/*
 * FrmPathfinderMain.cs
 * 
 * This is the main Form file of the A290 Pathinding Visualizer
 * 
 * Author: Sean Goldthwaite
 * Date Created: 11/24/2020
 * Last Modified by: Sean Goldthwaite
 * Date Last Modified: 12/11/2020
 * Assignment: Final Project Phase 3
 */

using System;
using System.Timers;
using System.Collections.Generic;
using System.Data;
using System.Drawing;
using System.Linq;
using System.Windows.Forms;

namespace PathfindingVisualizer
{
    public partial class FrmPathfinderMain : Form
    {
        //Timer for advancing algorithm with .Elapsed events
        private static System.Timers.Timer timer = new System.Timers.Timer();

        //2D array of Rectangles that stores all the rectangles in the grid.
        private Rectangle[,] rectangles = new Rectangle[40, 40];
        private const int rectSize = 20;

        //data logging variables about algorithm performance
        private int StepsTaken;
        private int NodesExpanded;

        //Grraphics object for drawing the board to the screen
        private Graphics gr;

        //SolidBrushes to be used with Graphics.FillRectangle method for visited, obstacles, start, and end squares
        private SolidBrush EmptyBrush = new SolidBrush(Color.White);
        private SolidBrush StartBrush = new SolidBrush(Color.Green);
        private SolidBrush EndBrush = new SolidBrush(Color.Red);
        private SolidBrush VisitedBrush = new SolidBrush(Color.LightGreen);
        private SolidBrush ObstaclesBrush = new SolidBrush(Color.Gray);

        //Pen to be used with Graphics.DrawRectangle method for all empty squares on the board
        private Pen emptyPen = Pens.Black;

        //HashSets of points that contain coordinates that have been visited or are obstacles
        //HashSets instead of Lists because HashSets don't allow duplicates
        private HashSet<Point> visited;
        private HashSet<Point> obstacles;

        //Points representing the start and end points of the algorithms
        private Point start;
        private Point end;

        //Points that track that previous start and end points, used for quickly updating the board without redrawing the entire grid
        private Point previousStart;
        private Point previousEnd;

        //bools tracking whether the user is currently setting obstacles, selecting a start, or selecting an end
        private bool SettingObstacles;
        private bool SelectingStart;
        private bool SelectingEnd;

        //bool tracking if the algorithm has started searching
        private bool Searching;

        //bool tracking if the grid has been properly loaded. 
        //Sometimes it is drawn on initializing the form and sometimes it isnt (I genuinely dont know why. After changing anything in the code it is always drawn on load but after a few runs it stops being drawn)
        //Regardless, the first MouseDown event on the form draws the grid
        private bool GridLoaded;

        //A List of points that mouse moves over, exclusively after the Set Obstacles button has been clicked
        private List<Point> MousePath;

        //EventHandlers that are dynamically subscribed to FrmPathfinderMain events. Used by Set Obstacles, Select Start, Select End buttons.
        private MouseEventHandler MouseMoveEventHandler;
        private MouseEventHandler MouseDownEventHandler;
        private MouseEventHandler MouseUpEventHandler;

        //Data structures used by the searchign algorithms
        private Stack<List<Point>> stack;
        private Queue<List<Point>> queue;
        private List<KeyValuePair<List<Point>, double>> Paths;
        private List<List<Point>> orderedList;

        public FrmPathfinderMain()
        {
            InitializeComponent();

            MouseMoveEventHandler = new MouseEventHandler(this.PathfinderMain_MouseMove);
            MouseDownEventHandler = new MouseEventHandler(this.PathfinderMain_MouseDown);
            MouseUpEventHandler = new MouseEventHandler(this.PathfinderMain_MouseUp);

            //Initializes 2D rectangles array
            for (int row = 0; row < rectangles.GetLength(0); row++)
            {
                for (int col = 0; col < rectangles.GetLength(1); col++)
                {
                    rectangles[row, col] = new Rectangle(rectSize + col * rectSize, rectSize + row * rectSize, rectSize, rectSize);
                }
            }

            Reset();
        }
        private void Reset()
        {
            if (timer.Enabled)
                timer.Stop();

            start = Point.Empty;
            end = Point.Empty;
            previousStart = Point.Empty;
            previousEnd = Point.Empty;

            StepsTaken = 0;
            LblStepsTaken.Text = "Steps Taken: " + StepsTaken;

            NodesExpanded = 0;
            LblNodesExpanded.Text = "Nodes Expanded: " + NodesExpanded;

            LblFound.Text = "";

            visited = new HashSet<Point>();
            obstacles = new HashSet<Point>();
            Paths = new List<KeyValuePair<List<Point>, double>>();

            CboAlgorithmSelector.SelectedIndex = 0;

            SettingObstacles = false;
            SelectingStart = false;
            SelectingEnd = false;
            Searching = false;

            stack = new Stack<List<Point>>();
            queue = new Queue<List<Point>>();
            orderedList = new List<List<Point>>();
            
            TrkSpeedSlider.Value = (TrkSpeedSlider.Maximum - TrkSpeedSlider.Minimum) / 2;
            LblSliderValue.Text = "Clock speed: " + TrkSpeedSlider.Value.ToString() + "ms"; 

            timer = new System.Timers.Timer();
            timer.Interval = TrkSpeedSlider.Value;
            timer.AutoReset = true;

            //makes sure all buttons are in the correct starting state
            CboAlgorithmSelector.Enabled = true;
            BtnSelectStart.Enabled = true;
            BtnSelectEnd.Enabled = true;
            BtnSetObstacles.Enabled = true;
            BtnBegin.Enabled = true;
            BtnManualControl.Enabled = true;

            BtnPause.Enabled = false;
            BtnTakeStep.Enabled = false;

            //Unsubscribes algorithm methods in case they were previously subscribed to the Elapsed event from a previous run
            timer.Elapsed -= DFS;
            timer.Elapsed -= BFS;
            timer.Elapsed -= AStar;
            timer.Elapsed -= UCS;
            timer.Elapsed -= Random;

            //Unsubscribes algorithm methods in case they were previously subscribed to the Click event of BtnTakeStep from a previous run
            BtnTakeStep.Click -= DFS;
            BtnTakeStep.Click -= BFS;
            BtnTakeStep.Click -= AStar;
            BtnTakeStep.Click -= UCS;
            BtnTakeStep.Click -= Random;

            //listens for a MouseDown event from the form to make sure the grid is drawn properly
            this.MouseDown += MouseDownEventHandler;


            //draws the full grid
            UpdateBoard();
        }
        private void BtnReset_Click(object sender, EventArgs e) => Reset();

        //Functions similar to reset, but does not clear start, end, and obstacles
        private void BtnClearBoard_Click(object sender, EventArgs e)
        {
            if (timer.Enabled)
                timer.Stop();

            StepsTaken = 0;
            LblStepsTaken.Text = "Steps Taken: " + StepsTaken;

            NodesExpanded = 0;
            LblNodesExpanded.Text = "Nodes Expanded: " + NodesExpanded;

            LblFound.Text = "";

            timer = new System.Timers.Timer();
            timer.Interval = TrkSpeedSlider.Value;
            timer.AutoReset = true;

            visited = new HashSet<Point>();
            Paths = new List<KeyValuePair<List<Point>, double>>();

            SettingObstacles = false;
            SelectingStart = false;
            SelectingEnd = false;
            Searching = false;

            stack = new Stack<List<Point>>();
            queue = new Queue<List<Point>>();
            orderedList = new List<List<Point>>();

            //makes sure all buttons are in the correct starting state
            CboAlgorithmSelector.Enabled = true;
            BtnSelectStart.Enabled = true;
            BtnSelectEnd.Enabled = true;
            BtnSetObstacles.Enabled = true;
            BtnBegin.Enabled = true;
            BtnManualControl.Enabled = true;

            BtnPause.Enabled = false;
            BtnTakeStep.Enabled = false;

            //Unsubscribes algorithm methods in case they were previously subscribed to the Elapsed event from a previous run
            timer.Elapsed -= DFS;
            timer.Elapsed -= BFS;
            timer.Elapsed -= AStar;
            timer.Elapsed -= UCS;
            timer.Elapsed -= Random;

            //Unsubscribes algorithm methods in case they were previously subscribed to the Click event of BtnTakeStep from a previous run
            BtnTakeStep.Click -= DFS;
            BtnTakeStep.Click -= BFS;
            BtnTakeStep.Click -= AStar;
            BtnTakeStep.Click -= UCS;
            BtnTakeStep.Click -= Random;

            //draws the full grid
            UpdateBoard();
        }

        private void BtnExit_Click(object sender, EventArgs e)
        {
            if (Searching) //If searching has started
            {
                //Asks the user if they are sure they want to exit
                if (MessageBox.Show("Are you sure you want to exit", "Attention", MessageBoxButtons.YesNo) == DialogResult.Yes)
                    Close();
                else //if not sure, does nothing
                    return;
            }
            else
            {
                Close();
            }
        }


        private void PathfinderMain_MouseDown(object sender, MouseEventArgs e)
        {
            if (!GridLoaded)
            {
                UpdateBoard();
                GridLoaded = true;
                this.MouseDown -= MouseDownEventHandler;
            }
            //if the mouse is clicked within the region of the grid
            if (e.X >= 20 && e.X < 820 && e.Y >= 20 && e.Y < 820)
            {
                //converts the Form coordinates to the index in the rectangle in the 2D array the mouse is over
                Point p = new Point((e.X - rectSize) / rectSize, (e.Y - rectSize) / rectSize);

                //Selecting start
                if (SelectingStart)
                {
                    previousStart = start;
                    if (e.Button == MouseButtons.Left)
                        start = p;
                    else if (e.Button == MouseButtons.Right && p.Equals(start))
                        start = Point.Empty;

                    //Redraws the old start and the new start points
                    UpdateBoard(start);
                    UpdateBoard(previousStart);
                }
                //Selecting end
                else if (SelectingEnd)
                {
                    previousEnd = end;
                    if (e.Button == MouseButtons.Left)
                        end = p;
                    else if (e.Button == MouseButtons.Right && p.Equals(end))
                        end = Point.Empty;

                    //Redraws the old start and the new end points
                    UpdateBoard(end);
                    UpdateBoard(previousEnd);
                }
                //Setting obstacles
                else if (SettingObstacles)
                {
                    MousePath = new List<Point>();

                    //adds or removes point from obstacles depending on which mouse button was pressed
                    if (e.Button == MouseButtons.Left)
                        obstacles.Add(p);
                    else if (e.Button == MouseButtons.Right)
                        obstacles.Remove(p);

                    //updates the point added or removed
                    UpdateBoard(p);

                    //subscribes MouseMove and MouseUp handlers so user can "click and drag"
                    this.MouseMove += MouseMoveEventHandler;
                    this.MouseUp += MouseUpEventHandler;
                }
            }
        }
        private void PathfinderMain_MouseUp(object sender, MouseEventArgs e)
        {
            //Unsubcribes MouseMove and MouseUp handlers because they are no longer being used
            this.MouseMove -= MouseMoveEventHandler;
            this.MouseUp -= MouseUpEventHandler;

            //Resets the MousePath List
            MousePath.Clear();
        }
        private void PathfinderMain_MouseMove(object sender, MouseEventArgs e)
        {
            //Converts the cursor position to the rectangle index in the 2D array the cursor is over
            Point p = new Point((e.X - rectSize) / rectSize, (e.Y - rectSize) / rectSize);

            try
            {
                //if this point is not already logged in MousePath
                if (!MousePath.Contains(p))
                {
                    //Logs the point in MousePath
                    MousePath.Add(p);

                    //if the left mouse button is being pressed
                    if (e.Button == MouseButtons.Left)
                    {
                        //adds point to obstacles and redraws that point
                        obstacles.Add(p);
                        UpdateBoard(p);
                    }
                    //if the right mouse button is being pressed
                    else if (e.Button == MouseButtons.Right)
                    {
                        //removes the point from obstacles and redraws that point
                        //if p is not in obstacles, nothing will change
                        obstacles.Remove(p);
                        UpdateBoard(p);
                    }
                }
            }
            catch (IndexOutOfRangeException OutOfRange)
            {
                //occurs when the user leaves the grid while still pressing a mousebutton while setting obstacles
                //simply catching the Exception is enough to make the program function as desired
            }
        }

        private bool InitializeAlgorithm()
        {
            if (!start.IsEmpty && !end.IsEmpty && !Searching)
            {
                //removes the end point from the obstacles
                obstacles.Remove(end);

                //Each case subscribes the proper methods to the Elapsed and Click methods that advance the algorithm
                //then properly initialize the data structure used by the algorithm with the initial values
                switch (CboAlgorithmSelector.SelectedIndex)
                {
                    case 0: //A*
                        timer.Elapsed += AStar;
                        BtnTakeStep.Click += AStar;
                        foreach (Point edge in getEdges(start))
                        {
                            List<Point> NewList = new List<Point>();
                            NewList.Add(edge);
                            Paths.Add(new KeyValuePair<List<Point>, double>(NewList, getDistance(start, edge)));
                        }
                        Paths = Paths.OrderBy(path => path.Value + getDistance(path.Key.Last(), end)).ToList();
                        break;
                    case 1: //UCS
                        timer.Elapsed += UCS;
                        BtnTakeStep.Click += UCS;
                        foreach (Point edge in getEdges(start))
                        {
                            List<Point> NewList = new List<Point>();
                            NewList.Add(edge);
                            Paths.Add(new KeyValuePair<List<Point>, double>(NewList, getDistance(edge, end)));
                        }
                        Paths = Paths.OrderBy(path => path.Value).ToList();
                        break;
                    case 2: //DFS
                        timer.Elapsed += DFS;
                        BtnTakeStep.Click += DFS;
                        foreach (Point edge in getEdges(start))
                        {
                            List<Point> NewList = new List<Point>();
                            NewList.Add(edge);
                            stack.Push(NewList);
                        }
                        break;
                    case 3: //BFS
                        timer.Elapsed += BFS;
                        BtnTakeStep.Click += BFS;
                        foreach (Point edge in getEdges(start))
                        {
                            List<Point> NewList = new List<Point>();
                            NewList.Add(edge);
                            queue.Enqueue(NewList);
                        }
                        break;
                    case 4: //Random
                        timer.Elapsed += Random;
                        BtnTakeStep.Click += Random;
                        foreach (Point edge in getEdges(start))
                        {
                            List<Point> NewList = new List<Point>();
                            NewList.Add(edge);
                            orderedList.Add(NewList);
                        }
                        break;
                }

                //Once searching has started these buttons shouldn't be used
                CboAlgorithmSelector.Enabled = false;
                BtnSelectStart.Enabled = false;
                BtnSelectEnd.Enabled = false;
                BtnSetObstacles.Enabled = false;

                //searching has now started
                Searching = true;
            }
            return Searching;
        }
        private void BtnBegin_Click(object sender, EventArgs e)
        {
            
            if (InitializeAlgorithm())
            {
                timer.Start();

                BtnPause.Enabled = true;
                BtnManualControl.Enabled = true;

                BtnBegin.Enabled = false;
                BtnTakeStep.Enabled = false;

                BtnPause.Focus();
            }
            else
            {
                MessageBox.Show("Please select a start and end point");
            }
        }

        private void BtnManualControl_Click(object sender, EventArgs e)
        {
            if (timer.Enabled)
            {
                timer.Stop();
                BtnBegin.Enabled = true;
                BtnTakeStep.Enabled = true;

                BtnPause.Enabled = false;
            }
            if (InitializeAlgorithm())
            {
                BtnBegin.Enabled = true;
                BtnTakeStep.Enabled = true;

                BtnPause.Enabled = false;
                BtnManualControl.Enabled = false;

                BtnTakeStep.Focus();
            }
            else
            {
                MessageBox.Show("Please select a start and end point");
            }
        }

        private void BtnPause_Click(object sender, EventArgs e)
        {
            if (timer.Enabled)
            {
                timer.Stop();

                BtnBegin.Enabled = true;

                BtnPause.Enabled = false;

                BtnBegin.Text = "Resume";
                BtnBegin.Focus();
            }
        }

        private void BtnSelectStart_Click(object sender, EventArgs e)
        {
            SelectingStart = !SelectingStart;

            if (SelectingStart)
            {
                this.MouseDown += MouseDownEventHandler;
            }
            else
            {
                this.MouseDown -= MouseDownEventHandler;
            }

            SettingObstacles = false;
            SelectingEnd = false;
        }

        private void BtnSelectEnd_Click(object sender, EventArgs e)
        {
            SelectingEnd = !SelectingEnd;

            if (SelectingEnd)
            {
                this.MouseDown += MouseDownEventHandler;
            }
            else
            {
                this.MouseDown -= MouseDownEventHandler;
            }

            SelectingStart = false;
            SettingObstacles = false;
        }

        private void BtnSetObstacles_Click(object sender, EventArgs e)
        {
            SettingObstacles = !SettingObstacles;

            if (SettingObstacles)
            {
                this.MouseDown += MouseDownEventHandler;
            }
            else
            {
                this.MouseDown -= MouseDownEventHandler;
            }

            SelectingEnd = false;
            SelectingStart = false;
        }

        private void BtnSelectStart_Leave(object sender, EventArgs e)
        {
            this.MouseDown -= MouseDownEventHandler;
            SelectingStart = false;
        }

        private void BtnSelectEnd_Leave(object sender, EventArgs e)
        {
            this.MouseDown -= MouseDownEventHandler;
            SelectingEnd = false;
        }

        private void BtnSetObstacles_Leave(object sender, EventArgs e)
        {
            this.MouseDown -= MouseDownEventHandler;
            SettingObstacles = false;
        }

        //TrkSpeedSlider.MouseUp only fires when the slider is at its final stop
        private void TrkSpeedSlider_MouseUp(object sender, MouseEventArgs e)
        {
            //Sets the interval of the timer to the selected value
            timer.Interval = TrkSpeedSlider.Value;

            //Updates the text displayed
            LblSliderValue.Text = "Clock speed: " + TrkSpeedSlider.Value.ToString() + "ms";
        }
        //TrkSpeedSlider.Scroll fires any time the TrackBar is moved
        private void TrkSpeedSlider_Scroll(object sender, EventArgs e)
        {
            //Just updates the text displayed
            LblSliderValue.Text = "Clock speed: " + TrkSpeedSlider.Value.ToString() + "ms";
        }

        private void FrmPathfinderMain_Shown(object sender, EventArgs e)
        {
            Reset();
        }

        //Methods that control the pathfinding algorithms
        private void AStar(object sender, EventArgs e)
        {
            if (Paths.Count > 0 && Paths.First().Key.Count > 0)
            {
                LblStepsTaken.Text = "Steps Taken: " + ++StepsTaken;
                var Kvp = Paths.First();
                var Path = Kvp.Key;
                double cost = Kvp.Value;
                Paths.RemoveAt(0);
                if (Path.Last().Equals(end))
                {
                    Found(Path);
                }
                if (!visited.Contains(Path.Last()))
                {
                    visited.Add(Path.Last());
                    UpdateBoard(visited.ToList());
                    UpdateBoard(Path, new SolidBrush(Color.Blue));

                    List<Point> edges = getEdges(Path.Last());
                    LblNodesExpanded.Text = "Nodes Expanded: " + ++NodesExpanded;
                    foreach (Point edge in edges)
                    {
                        List<Point> NewList = new List<Point>(Path);
                        NewList.Add(edge);
                        Paths.Add(new KeyValuePair<List<Point>, double>(NewList, cost + getDistance(Path.Last(), edge)));
                    }
                    Paths = Paths.OrderBy(path => path.Value + getDistance(path.Key.Last(), end)).ToList();
                }
            }
            else
            {
                LblFound.Text = "No path found";
                timer.Stop();
                //timer.Dispose();
            }
        }
        private void AStar(object sender, ElapsedEventArgs e) => AStar(sender, new EventArgs());
        private void UCS(object sender, EventArgs e)
        {
            if (Paths.Count > 0 && Paths.First().Key.Count > 0)
            {
                LblStepsTaken.Text = "Steps Taken: " + ++StepsTaken;
                var Kvp = Paths.First();
                var Path = Kvp.Key;
                double cost = Kvp.Value;
                Paths.RemoveAt(0);
                if (Path.Last().Equals(end))
                {
                    Found(Path);
                }
                if (!visited.Contains(Path.Last()))
                {
                    visited.Add(Path.Last());
                    UpdateBoard(visited.ToList());
                    UpdateBoard(Path, new SolidBrush(Color.Blue));

                    List<Point> edges = getEdges(Path.Last());
                    LblNodesExpanded.Text = "Nodes Expanded: " + ++NodesExpanded;
                    foreach (Point edge in edges)
                    {
                        List<Point> NewList = new List<Point>(Path);
                        NewList.Add(edge);
                        Paths.Add(new KeyValuePair<List<Point>, double>(NewList, getDistance(edge, end)));
                    }
                    Paths = Paths.OrderBy(path => path.Value).ToList();
                }
            }
            else
            {
                LblFound.Text = "No path found";
                timer.Stop();
                timer.Dispose();
            }
        }
        private void UCS(object sender, ElapsedEventArgs e) => UCS(sender, new EventArgs());
        private void DFS(object sender, EventArgs e)
        {
            if (stack.Count > 0 && stack.Peek().Count > 0)
            {
                LblStepsTaken.Text = "Steps Taken: " + ++StepsTaken;
                List<Point> Path = stack.Pop();
                if (Path.Last().Equals(end))
                {
                    Found(Path);
                }
                if (!visited.Contains(Path.Last()))
                {
                    visited.Add(Path.Last());
                    UpdateBoard(visited.ToList());
                    UpdateBoard(Path, new SolidBrush(Color.Blue));

                    List<Point> edges = getEdges(Path.Last());
                    LblNodesExpanded.Text = "Nodes Expanded: " + ++NodesExpanded;
                    foreach (Point edge in edges)
                    {
                        List<Point> NewList = new List<Point>(Path);
                        NewList.Add(edge);
                        stack.Push(NewList);
                    }
                }
            }
            else
            {
                LblFound.Text = "No path found";
                timer.Stop();
                timer.Dispose();
            }
        }
        private void DFS(object sender, ElapsedEventArgs e) => DFS(sender, new EventArgs());
        private void BFS(object sender, EventArgs e)
        {
            if (queue.Count > 0 && queue.Peek().Count > 0)
            {
                LblStepsTaken.Text = "Steps Taken: " + ++StepsTaken;
                List<Point> Path = queue.Dequeue();
                if (Path.Last().Equals(end))
                {
                    Found(Path);
                }
                if (!visited.Contains(Path.Last()))
                {
                    visited.Add(Path.Last());
                    UpdateBoard(visited.ToList());
                    UpdateBoard(Path, new SolidBrush(Color.Blue));

                    List<Point> edges = getEdges(Path.Last());
                    LblNodesExpanded.Text = "Nodes Expanded: " + ++NodesExpanded;
                    foreach (Point edge in edges)
                    {
                        List<Point> NewList = new List<Point>(Path);
                        NewList.Add(edge);
                        queue.Enqueue(NewList);
                    }
                }
            }
            else
            {
                LblFound.Text = "No path found";
                timer.Stop();
                timer.Dispose();
            }
        }
        private void BFS(object sender, ElapsedEventArgs e) => BFS(sender, new EventArgs());
        private void Random(object sender, EventArgs e)
        {
            if (orderedList.Count > 0)
            {
                int r = new Random().Next(0, orderedList.Count);
                List<Point> Path = orderedList.ElementAt(r);
                orderedList.RemoveAt(r);
                if (Path.Count > 0)
                {
                    LblStepsTaken.Text = "Steps Taken: " + ++StepsTaken;
                    if (Path.Last().Equals(end))
                    {
                        Found(Path);
                    }
                    if (!visited.Contains(Path.Last()))
                    {
                        visited.Add(Path.Last());
                        UpdateBoard(visited.ToList());
                        UpdateBoard(Path, new SolidBrush(Color.Blue));

                        List<Point> edges = getEdges(Path.Last());
                        LblNodesExpanded.Text = "Nodes Expanded: " + ++NodesExpanded;
                        foreach (Point edge in edges)
                        {
                            List<Point> NewList = new List<Point>(Path);
                            NewList.Add(edge);
                            orderedList.Add(NewList);
                        }
                    }
                }
            }
            else
            {
                LblFound.Text = "No path found";
                timer.Stop();
                timer.Dispose();
            }
        }
        private void Random(object sender, ElapsedEventArgs e) => Random(sender, new EventArgs());

        //Method is run whenever an algorithm has found the endpoint
        private void Found(List<Point> Path)
        {
            LblFound.Text = string.Format("Found!\nPath Cost: {0:0.00}", getPathCost(Path) + getDistance(start, Path.First()))
                            + string.Format("\nShortest Distance: {0:0.00}", getDistance(start, end))
                            + string.Format("\nEfficiency: {0:0.00}%", (getDistance(start, end) / (getPathCost(Path) + getDistance(start, Path.First()))) * 100);

            //the last point is also the end point
            //removes it so the draw call does not draw over the end point
            Path.RemoveAt(Path.Count - 1);
            UpdateBoard(visited.ToList());
            UpdateBoard(Path, new SolidBrush(Color.Black));
            timer.Stop();
            //timer.Dispose();

            BtnBegin.Enabled = false;
            BtnPause.Enabled = false;
            BtnManualControl.Enabled = false;
            BtnTakeStep.Enabled = false;
        }

        //returns a List of Points that represent the adjacent tiles to Point p, minus any previously visited tiles or obstacle tiles
        private List<Point> getEdges(Point p)
        {
            List<Point> edges = new List<Point>();

            //Extra checks for diagonals to make sure edges cannot "phase through" diagonal walls
            //Example: Obstacles on north and east face of point, the north-east point should not be considered an edge of the node.

            //Top
            if (p.X < 39)
                edges.Add(new Point(p.X, p.Y + 1));
            //Top-Right
            if (p.X < 39 && p.Y >= 1 && !obstacles.Contains(new Point(p.X + 1, p.Y)) && !obstacles.Contains(new Point(p.X, p.Y - 1)))
                edges.Add(new Point(p.X + 1, p.Y - 1));
            //Right
            if (p.X < 39)
                edges.Add(new Point(p.X + 1, p.Y));
            //Bottom-Right
            if (p.X < 39 && p.Y < 39 && !obstacles.Contains(new Point(p.X + 1, p.Y)) && !obstacles.Contains(new Point(p.X, p.Y + 1)))
                edges.Add(new Point(p.X + 1, p.Y + 1));
            //Bottom
            if (p.Y >= 1)
                edges.Add(new Point(p.X, p.Y - 1));
            //Bottom-Left
            if (p.X >= 1 && p.Y < 39 && !obstacles.Contains(new Point(p.X - 1, p.Y)) && !obstacles.Contains(new Point(p.X, p.Y + 1)))
                edges.Add(new Point(p.X - 1, p.Y + 1));
            //Left
            if (p.X >= 1)
                edges.Add(new Point(p.X - 1, p.Y));
            //Top-Left
            if (p.X >= 1 && p.Y >= 1 && !obstacles.Contains(new Point(p.X - 1, p.Y)) && !obstacles.Contains(new Point(p.X, p.Y - 1)))
                edges.Add(new Point(p.X - 1, p.Y - 1));

            //Removes any points that are in either obstacles or visited lists
            edges.RemoveAll(p => obstacles.Contains(p) || visited.Contains(p));

            return edges;
        }


        //Methods for drawing the grid

        //Draws the entire 40x40 grid
        private void UpdateBoard()
        {
            gr = CreateGraphics();

            Point p = Point.Empty;
            for (int row = 0; row < rectangles.GetLength(0); row++)
            {
                for (int col = 0; col < rectangles.GetLength(1); col++)
                {
                    p = new Point(col, row);

                    if (!start.IsEmpty && start.Equals(p))
                        gr.FillRectangle(StartBrush, rectangles[row, col]);
                    else if (!end.IsEmpty && end.Equals(p))
                        gr.FillRectangle(EndBrush, rectangles[row, col]);
                    else if (visited.Contains(p))
                        gr.FillRectangle(VisitedBrush, rectangles[row, col]);
                    else if (obstacles.Contains(p))
                        gr.FillRectangle(ObstaclesBrush, rectangles[row, col]);
                    else
                    {
                        gr.FillRectangle(EmptyBrush, rectangles[row, col]);
                        gr.DrawRectangle(emptyPen, rectangles[row, col]);
                    }
                }
            }
            gr.Dispose();
        }

        //draws only the cells of the board in points List
        private void UpdateBoard(List<Point> points)
        {
            gr = CreateGraphics();
            foreach (Point p in points)
            {
                if (!start.IsEmpty && start.Equals(p))
                    gr.FillRectangle(StartBrush, rectangles[p.Y, p.X]);
                else if (!end.IsEmpty && end.Equals(p))
                    gr.FillRectangle(EndBrush, rectangles[p.Y, p.X]);
                else if (visited.Contains(p))
                    gr.FillRectangle(VisitedBrush, rectangles[p.Y, p.X]);
                else if (obstacles.Contains(p))
                    gr.FillRectangle(ObstaclesBrush, rectangles[p.Y, p.X]);
                else
                {
                    gr.FillRectangle(EmptyBrush, rectangles[p.Y, p.X]);
                    gr.DrawRectangle(emptyPen, rectangles[p.Y, p.X]);
                }
            }
            gr.Dispose();
        }
        //Draws the Points in points with the specified SolidBrush
        private void UpdateBoard(List<Point> points, SolidBrush Brush)
        {
            gr = CreateGraphics();
            foreach (Point p in points)
            {
                gr.FillRectangle(Brush, rectangles[p.Y, p.X]);
            }
            gr.Dispose();
        }
        //Draws a single point on the board
        private void UpdateBoard(Point p)
        {
            gr = CreateGraphics();
            if (!start.IsEmpty && start.Equals(p))
                gr.FillRectangle(StartBrush, rectangles[p.Y, p.X]);
            else if (!end.IsEmpty && end.Equals(p))
                gr.FillRectangle(EndBrush, rectangles[p.Y, p.X]);
            else if (visited.Contains(p))
                gr.FillRectangle(VisitedBrush, rectangles[p.Y, p.X]);
            else if (obstacles.Contains(p))
                gr.FillRectangle(ObstaclesBrush, rectangles[p.Y, p.X]);
            else
            {
                gr.FillRectangle(EmptyBrush, rectangles[p.Y, p.X]);
                gr.DrawRectangle(emptyPen, rectangles[p.Y, p.X]);
            }
            gr.Dispose();
        }

        //Helper methods for the pathfinding algorithms

        //Calculates euclidean distance between 2 points
        private double getDistance(Point p1, Point p2)
        {
            return Math.Sqrt(Math.Pow(p1.X - p2.X, 2) + Math.Pow(p1.Y - p2.Y, 2));
        }
        //Calculates sum of euclidean distance between each consequtive point in Points
        private double getPathCost(List<Point> points)
        {
            double cost = 0;
            for (int i = 1; i < points.Count; i++)
            {
                cost += getDistance(points.ElementAt(i - 1), points.ElementAt(i));
            }
            return cost;
        }
    }
}