/*
 * FrmPathfinderMain.Designer.cs
 * 
 * This is the Designer Form file of the A290 Pathinding Visualizer
 * 
 * Author: Sean Goldthwaite
 * Date Created: 11/24/2020
 * Last Modified by: Sean Goldthwaite
 * Date Last Modified: 12/11/2020
 * Assignment: Final Project Phase 3
 */

namespace PathfindingVisualizer
{
    partial class FrmPathfinderMain
    {
        /// <summary>
        ///  Required designer variable.
        /// </summary>
        private System.ComponentModel.IContainer components = null;

        /// <summary>
        ///  Clean up any resources being used.
        /// </summary>
        /// <param name="disposing">true if managed resources should be disposed; otherwise, false.</param>
        protected override void Dispose(bool disposing)
        {
            if (disposing && (components != null))
            {
                components.Dispose();
            }
            base.Dispose(disposing);
        }

        #region Windows Form Designer generated code

        /// <summary>
        ///  Required method for Designer support - do not modify
        ///  the contents of this method with the code editor.
        /// </summary>
        private void InitializeComponent()
        {
            this.BtnExit = new System.Windows.Forms.Button();
            this.LblFound = new System.Windows.Forms.Label();
            this.BtnBegin = new System.Windows.Forms.Button();
            this.BtnSelectStart = new System.Windows.Forms.Button();
            this.BtnSelectEnd = new System.Windows.Forms.Button();
            this.BtnSetObstacles = new System.Windows.Forms.Button();
            this.BtnManualControl = new System.Windows.Forms.Button();
            this.BtnPause = new System.Windows.Forms.Button();
            this.CboAlgorithmSelector = new System.Windows.Forms.ComboBox();
            this.BtnTakeStep = new System.Windows.Forms.Button();
            this.FplGrid = new System.Windows.Forms.FlowLayoutPanel();
            this.TrkSpeedSlider = new System.Windows.Forms.TrackBar();
            this.BtnReset = new System.Windows.Forms.Button();
            this.LblStepsTaken = new System.Windows.Forms.Label();
            this.LblSliderValue = new System.Windows.Forms.Label();
            this.LblNodesExpanded = new System.Windows.Forms.Label();
            this.BtnClearBoard = new System.Windows.Forms.Button();
            ((System.ComponentModel.ISupportInitialize)(this.TrkSpeedSlider)).BeginInit();
            this.SuspendLayout();
            // 
            // BtnExit
            // 
            this.BtnExit.Location = new System.Drawing.Point(823, 612);
            this.BtnExit.Margin = new System.Windows.Forms.Padding(3, 2, 3, 2);
            this.BtnExit.Name = "BtnExit";
            this.BtnExit.Size = new System.Drawing.Size(148, 38);
            this.BtnExit.TabIndex = 11;
            this.BtnExit.Text = "Exit";
            this.BtnExit.UseVisualStyleBackColor = true;
            this.BtnExit.Click += new System.EventHandler(this.BtnExit_Click);
            // 
            // LblFound
            // 
            this.LblFound.AutoSize = true;
            this.LblFound.Location = new System.Drawing.Point(823, 682);
            this.LblFound.Name = "LblFound";
            this.LblFound.Size = new System.Drawing.Size(39, 15);
            this.LblFound.TabIndex = 14;
            this.LblFound.Text = "found";
            // 
            // BtnBegin
            // 
            this.BtnBegin.Location = new System.Drawing.Point(823, 233);
            this.BtnBegin.Margin = new System.Windows.Forms.Padding(3, 2, 3, 2);
            this.BtnBegin.Name = "BtnBegin";
            this.BtnBegin.Size = new System.Drawing.Size(148, 38);
            this.BtnBegin.TabIndex = 4;
            this.BtnBegin.Text = "Begin";
            this.BtnBegin.UseVisualStyleBackColor = true;
            this.BtnBegin.Click += new System.EventHandler(this.BtnBegin_Click);
            // 
            // BtnSelectStart
            // 
            this.BtnSelectStart.Location = new System.Drawing.Point(824, 47);
            this.BtnSelectStart.Margin = new System.Windows.Forms.Padding(3, 2, 3, 2);
            this.BtnSelectStart.Name = "BtnSelectStart";
            this.BtnSelectStart.Size = new System.Drawing.Size(148, 38);
            this.BtnSelectStart.TabIndex = 1;
            this.BtnSelectStart.Text = "Select Start";
            this.BtnSelectStart.UseVisualStyleBackColor = true;
            this.BtnSelectStart.Click += new System.EventHandler(this.BtnSelectStart_Click);
            this.BtnSelectStart.Leave += new System.EventHandler(this.BtnSelectStart_Leave);
            // 
            // BtnSelectEnd
            // 
            this.BtnSelectEnd.Location = new System.Drawing.Point(824, 89);
            this.BtnSelectEnd.Margin = new System.Windows.Forms.Padding(3, 2, 3, 2);
            this.BtnSelectEnd.Name = "BtnSelectEnd";
            this.BtnSelectEnd.Size = new System.Drawing.Size(148, 38);
            this.BtnSelectEnd.TabIndex = 2;
            this.BtnSelectEnd.Text = "Select End";
            this.BtnSelectEnd.UseVisualStyleBackColor = true;
            this.BtnSelectEnd.Click += new System.EventHandler(this.BtnSelectEnd_Click);
            this.BtnSelectEnd.Leave += new System.EventHandler(this.BtnSelectEnd_Leave);
            // 
            // BtnSetObstacles
            // 
            this.BtnSetObstacles.Location = new System.Drawing.Point(824, 131);
            this.BtnSetObstacles.Margin = new System.Windows.Forms.Padding(3, 2, 3, 2);
            this.BtnSetObstacles.Name = "BtnSetObstacles";
            this.BtnSetObstacles.Size = new System.Drawing.Size(148, 38);
            this.BtnSetObstacles.TabIndex = 3;
            this.BtnSetObstacles.Text = "Set Obstacles";
            this.BtnSetObstacles.UseVisualStyleBackColor = true;
            this.BtnSetObstacles.Click += new System.EventHandler(this.BtnSetObstacles_Click);
            this.BtnSetObstacles.Leave += new System.EventHandler(this.BtnSetObstacles_Leave);
            // 
            // BtnManualControl
            // 
            this.BtnManualControl.Location = new System.Drawing.Point(824, 317);
            this.BtnManualControl.Margin = new System.Windows.Forms.Padding(3, 2, 3, 2);
            this.BtnManualControl.Name = "BtnManualControl";
            this.BtnManualControl.Size = new System.Drawing.Size(148, 38);
            this.BtnManualControl.TabIndex = 6;
            this.BtnManualControl.Text = "Manual Control";
            this.BtnManualControl.UseVisualStyleBackColor = true;
            this.BtnManualControl.Click += new System.EventHandler(this.BtnManualControl_Click);
            // 
            // BtnPause
            // 
            this.BtnPause.Location = new System.Drawing.Point(823, 275);
            this.BtnPause.Margin = new System.Windows.Forms.Padding(3, 2, 3, 2);
            this.BtnPause.Name = "BtnPause";
            this.BtnPause.Size = new System.Drawing.Size(148, 38);
            this.BtnPause.TabIndex = 5;
            this.BtnPause.Text = "Pause";
            this.BtnPause.UseVisualStyleBackColor = true;
            this.BtnPause.Click += new System.EventHandler(this.BtnPause_Click);
            // 
            // CboAlgorithmSelector
            // 
            this.CboAlgorithmSelector.DropDownHeight = 150;
            this.CboAlgorithmSelector.DropDownStyle = System.Windows.Forms.ComboBoxStyle.DropDownList;
            this.CboAlgorithmSelector.FormattingEnabled = true;
            this.CboAlgorithmSelector.IntegralHeight = false;
            this.CboAlgorithmSelector.Items.AddRange(new object[] {
            "A* Pathfinding",
            "Dijkstra\'s Pathfinding",
            "Depth First Search",
            "Breadth First Search",
            "Random Search"});
            this.CboAlgorithmSelector.Location = new System.Drawing.Point(824, 20);
            this.CboAlgorithmSelector.Margin = new System.Windows.Forms.Padding(3, 2, 3, 2);
            this.CboAlgorithmSelector.Name = "CboAlgorithmSelector";
            this.CboAlgorithmSelector.Size = new System.Drawing.Size(148, 23);
            this.CboAlgorithmSelector.TabIndex = 0;
            // 
            // BtnTakeStep
            // 
            this.BtnTakeStep.Location = new System.Drawing.Point(824, 359);
            this.BtnTakeStep.Margin = new System.Windows.Forms.Padding(3, 2, 3, 2);
            this.BtnTakeStep.Name = "BtnTakeStep";
            this.BtnTakeStep.Size = new System.Drawing.Size(148, 38);
            this.BtnTakeStep.TabIndex = 7;
            this.BtnTakeStep.Text = "Take Step";
            this.BtnTakeStep.UseVisualStyleBackColor = true;
            // 
            // FplGrid
            // 
            this.FplGrid.Enabled = false;
            this.FplGrid.Location = new System.Drawing.Point(18, 20);
            this.FplGrid.Margin = new System.Windows.Forms.Padding(3, 2, 3, 2);
            this.FplGrid.Name = "FplGrid";
            this.FplGrid.Size = new System.Drawing.Size(800, 800);
            this.FplGrid.TabIndex = 13;
            this.FplGrid.Visible = false;
            // 
            // TrkSpeedSlider
            // 
            this.TrkSpeedSlider.Location = new System.Drawing.Point(823, 401);
            this.TrkSpeedSlider.Margin = new System.Windows.Forms.Padding(3, 2, 3, 2);
            this.TrkSpeedSlider.Maximum = 250;
            this.TrkSpeedSlider.Minimum = 5;
            this.TrkSpeedSlider.Name = "TrkSpeedSlider";
            this.TrkSpeedSlider.Size = new System.Drawing.Size(148, 45);
            this.TrkSpeedSlider.TabIndex = 8;
            this.TrkSpeedSlider.TickStyle = System.Windows.Forms.TickStyle.None;
            this.TrkSpeedSlider.Value = 10;
            this.TrkSpeedSlider.Scroll += new System.EventHandler(this.TrkSpeedSlider_Scroll);
            this.TrkSpeedSlider.MouseUp += new System.Windows.Forms.MouseEventHandler(this.TrkSpeedSlider_MouseUp);
            // 
            // BtnReset
            // 
            this.BtnReset.Location = new System.Drawing.Point(823, 570);
            this.BtnReset.Margin = new System.Windows.Forms.Padding(3, 2, 3, 2);
            this.BtnReset.Name = "BtnReset";
            this.BtnReset.Size = new System.Drawing.Size(148, 38);
            this.BtnReset.TabIndex = 10;
            this.BtnReset.Text = "Reset";
            this.BtnReset.UseVisualStyleBackColor = true;
            this.BtnReset.Click += new System.EventHandler(this.BtnReset_Click);
            // 
            // LblStepsTaken
            // 
            this.LblStepsTaken.AutoSize = true;
            this.LblStepsTaken.Location = new System.Drawing.Point(824, 652);
            this.LblStepsTaken.Name = "LblStepsTaken";
            this.LblStepsTaken.Size = new System.Drawing.Size(74, 15);
            this.LblStepsTaken.TabIndex = 12;
            this.LblStepsTaken.Text = "Steps Taken: ";
            // 
            // LblSliderValue
            // 
            this.LblSliderValue.AutoSize = true;
            this.LblSliderValue.Location = new System.Drawing.Point(824, 431);
            this.LblSliderValue.Name = "LblSliderValue";
            this.LblSliderValue.Size = new System.Drawing.Size(64, 15);
            this.LblSliderValue.TabIndex = 11;
            this.LblSliderValue.Text = "SliderValue";
            // 
            // LblNodesExpanded
            // 
            this.LblNodesExpanded.AutoSize = true;
            this.LblNodesExpanded.Location = new System.Drawing.Point(823, 667);
            this.LblNodesExpanded.Name = "LblNodesExpanded";
            this.LblNodesExpanded.Size = new System.Drawing.Size(102, 15);
            this.LblNodesExpanded.TabIndex = 10;
            this.LblNodesExpanded.Text = "Nodes Expanded: ";
            // 
            // BtnClearBoard
            // 
            this.BtnClearBoard.Location = new System.Drawing.Point(824, 527);
            this.BtnClearBoard.Name = "BtnClearBoard";
            this.BtnClearBoard.Size = new System.Drawing.Size(148, 38);
            this.BtnClearBoard.TabIndex = 9;
            this.BtnClearBoard.Text = "Clear Board";
            this.BtnClearBoard.UseVisualStyleBackColor = true;
            this.BtnClearBoard.Click += new System.EventHandler(this.BtnClearBoard_Click);
            // 
            // FrmPathfinderMain
            // 
            this.AutoScaleDimensions = new System.Drawing.SizeF(7F, 15F);
            this.AutoScaleMode = System.Windows.Forms.AutoScaleMode.Font;
            this.CancelButton = this.BtnExit;
            this.ClientSize = new System.Drawing.Size(984, 861);
            this.Controls.Add(this.BtnClearBoard);
            this.Controls.Add(this.LblNodesExpanded);
            this.Controls.Add(this.LblSliderValue);
            this.Controls.Add(this.LblStepsTaken);
            this.Controls.Add(this.BtnReset);
            this.Controls.Add(this.TrkSpeedSlider);
            this.Controls.Add(this.FplGrid);
            this.Controls.Add(this.BtnTakeStep);
            this.Controls.Add(this.CboAlgorithmSelector);
            this.Controls.Add(this.BtnPause);
            this.Controls.Add(this.BtnManualControl);
            this.Controls.Add(this.BtnSetObstacles);
            this.Controls.Add(this.BtnSelectEnd);
            this.Controls.Add(this.BtnSelectStart);
            this.Controls.Add(this.BtnBegin);
            this.Controls.Add(this.LblFound);
            this.Controls.Add(this.BtnExit);
            this.FormBorderStyle = System.Windows.Forms.FormBorderStyle.FixedSingle;
            this.Margin = new System.Windows.Forms.Padding(3, 2, 3, 2);
            this.MaximizeBox = false;
            this.Name = "FrmPathfinderMain";
            this.Text = "A290 Pathfinding Visualizer";
            this.Shown += new System.EventHandler(this.FrmPathfinderMain_Shown);
            ((System.ComponentModel.ISupportInitialize)(this.TrkSpeedSlider)).EndInit();
            this.ResumeLayout(false);
            this.PerformLayout();

        }

        #endregion
        private System.Windows.Forms.Button BtnExit;
        private System.Windows.Forms.Label LblFound;
        private System.Windows.Forms.Button BtnBegin;
        private System.Windows.Forms.Button BtnSelectStart;
        private System.Windows.Forms.Button BtnSelectEnd;
        private System.Windows.Forms.Button BtnSetObstacles;
        private System.Windows.Forms.Button BtnManualControl;
        private System.Windows.Forms.Button BtnPause;
        private System.Windows.Forms.ComboBox CboAlgorithmSelector;
        private System.Windows.Forms.Button BtnTakeStep;
        private System.Windows.Forms.FlowLayoutPanel FplGrid;
        private System.Windows.Forms.TrackBar TrkSpeedSlider;
        private System.Windows.Forms.Button BtnReset;
        private System.Windows.Forms.Label LblStepsTaken;
        private System.Windows.Forms.Label LblSliderValue;
        private System.Windows.Forms.Label LblNodesExpanded;
        private System.Windows.Forms.Button BtnClearBoard;
    }
}

