/*
 * FrmBuffetMain.cs
 * 
 * This is the main Form file of the A290 Buffet
 * 
 * Author: Sean Goldthwaite
 * Date Created: 11/04/2020
 * Last Modified by: Sean Goldthwaite
 * Date Last Modified: 11/17/2020
 * Assignment: Homework Assignment 2
 */

using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Data;
using System.Drawing;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Windows.Forms;

namespace Homework_Project_1
{
    public partial class FrmBuffetMain : Form
    {
        //Boolean that indicates whether an image has been elected or not.
        private bool imageSelected = false;

        //Boolean that indicates whether the border is visible or not
        private bool borderVisible = false;

        //Integer and Size object that handle the changing of size of the window and imagebox
        //Should not be changed unless by programmer, so readonly
        private readonly int sizeChangeAmount = 10;
        private readonly Size sizeChange = new Size(10, 10);

        //Instance variable that controls the drawing of the border
        private Graphics objBorder;

        //Stores the pen that is used to draw the border. Abstracted to eliminate StackOverflowException
        private Pen localBorderPen;

        //Property that other classes interact with to change the color of the border
        public Pen BorderPen
        {
            get { return localBorderPen; }
            set { localBorderPen = value; }
        }

        //instance variables that store values in FrmBuffetOptions
        //Located in main form so that FrmBuffetOptions always opens with previously selected items.
        public string OptionsImageBorderColorText;
        public string OptionsUsernameText;
        public string OptionsWindowBorderColorText;
        public Pen OptionsBorderColor;
        public RdbColorSelected OptionsBackgroundColor;

        //enum that represents the radio button selected in FrmBuffetOptions
        public enum RdbColorSelected
        {
            Red,
            Green,
            Blue,
            Default
        }
        public FrmBuffetMain()
        {
            InitializeComponent();

            //Initializes the color of the border to be Black
            BorderPen = Pens.Black;

            //Instanciates default values that FrmBuffetOptions will use
            OptionsImageBorderColorText = "Black";
            OptionsWindowBorderColorText = "Default";
            OptionsBorderColor = new Pen(DefaultBackColor, 2);
            OptionsBackgroundColor = RdbColorSelected.Default;
    }

        private void BtnQuit_Click(object sender, EventArgs e)
        {
            //When Quit button is clicked, prompts the user is they are sure they want to exit.
            //Does nothing if "No"
            if (ChkPromptOnExit.Checked)
            {
                if (MessageBox.Show("Are you sure you want to exit?", "Exit", MessageBoxButtons.YesNo) == DialogResult.Yes)
                {
                    //Gracefully exits the program when "Quit" button is clicked
                    Close();
                }
                else
                    return;
            }
            Close();

        }
        private void BtnOptions_Click(object sender, EventArgs e)
        {
            //Initialize and launches the FrmBuffetOptions form when options button is clicked.
            FrmBuffetOptions FrmBuffetOptionsDialog = new FrmBuffetOptions(this);
            FrmBuffetOptionsDialog.ShowDialog(this);
        }

        private void BtnCollections_Click(object sender, EventArgs e)
        {
            //Initialize and launches the FrmBuffetCollections form when options button is clicked.
            FrmBuffetCollections FrmBuffetCollectionsDialog = new FrmBuffetCollections();
            FrmBuffetCollectionsDialog.ShowDialog(this);
        }

        private void BtnSelectImage_Click(object sender, EventArgs e)
        {
            //Shows file select dialog and continues if user selected file
            if (OfdSelectPicture.ShowDialog() == DialogResult.OK)
            {
                //Displays selected image in image box
                ImgBox.Image = Image.FromFile(OfdSelectPicture.FileName);

                //Fits image to the given window height, then adjusts width based on the aspect ratio of the image
                double AspectRatio = ImgBox.Image.Width / ImgBox.Image.Height;
                ImgBox.Height = Height - 150;
                ImgBox.Width = Convert.ToInt32(ImgBox.Height * AspectRatio);
                Width = ImgBox.Width + 75;

                //Adds the file path of the image to the window title.
                Text = string.Concat("A290 Buffet (" + OfdSelectPicture.FileName + ")");
                LblImgDim.Text = "Image Dimensions: (" + ImgBox.Width + "," + ImgBox.Height + ")";

                imageSelected = true;
            }

            //Since the width/height of the image box can change, the border must be redrawn to accomodate. 
            RedrawBorder();
        }

        private void BtnDrawBorder_Click(object sender, EventArgs e)
        {
            //if the border is undrawn, draws it with color specified by aBorderPen
            //else, does nothing.
            if (!borderVisible)
            {

                objBorder = CreateGraphics();
                objBorder.Clear(SystemColors.Control);
                objBorder.DrawRectangle(localBorderPen, ImgBox.Left - 1, ImgBox.Top - 1, width: ImgBox.Width + 1, height: ImgBox.Height + 1);
                objBorder.Dispose();
                borderVisible = true;
            }

        }

        private void BtnHideBorder_Click(object sender, EventArgs e)
        {
            //If the border is drawn, undraws it
            //else, does nothing.
            if (borderVisible)
            {
                objBorder = CreateGraphics();
                objBorder.Clear(SystemColors.Control);
                objBorder.Dispose();
                borderVisible = false;
            }
        }

        public void RedrawBorder()
        {
            //If the border is currrently visible, redraws it with current settings of color, height, width. 
            if (borderVisible)
            {
                objBorder = CreateGraphics();
                objBorder.Clear(SystemColors.Control);
                objBorder.DrawRectangle(localBorderPen, ImgBox.Left - 1, ImgBox.Top - 1, width: ImgBox.Width + 1, height: ImgBox.Height + 1);
                objBorder.Dispose();
            }
        }

        private void ImgBox_MouseMove(object sender, MouseEventArgs e)
        {
            //When the mouse moves over the image box, the mouse coordinates are displayed in labels labeled X: and Y: 
            LblX.Text = "X: " + e.X;
            LblY.Text = "Y: " + e.Y;
        }

        private void ImgBox_MouseLeave(object sender, EventArgs e)
        {
            //When the mouse leaves the image box, coordinate labels are "reset" to a blank state as to not show the last recieved coordinate
            LblX.Text = "X: ";
            LblY.Text = "Y: ";
        }

        private void FrmBuffetMain_ResizeEnd(object sender, EventArgs e)
        {
            //If an image has not been selected yet
            if (!imageSelected)
            {
                ImgBox.Height = Height - 150;
                ImgBox.Width = Width - 75;
            }
            else //An image has been selected
            {
                //Fits image to the given window height, then adjusts width based on the aspect ratio of the image
                double AspectRatio = ImgBox.Image.Width / ImgBox.Image.Height;
                ImgBox.Height = Height - 150;
                ImgBox.Width = Convert.ToInt32(ImgBox.Height * AspectRatio);
                Width = ImgBox.Width + 75;
            }

            //Redraws the border because the size has been changed
            RedrawBorder();
        }

        private void BtnEnlarge_Click(object sender, EventArgs e)
        {
            //Increments the Size by constant sizeChange Size object
            Size += sizeChange;
            ImgBox.Size += sizeChange;

            //Moves the cursor so that it stays in the same place over the Enlarge button.
            Cursor.Position = new Point(Cursor.Position.X, Cursor.Position.Y + sizeChangeAmount);

            //The window size and therefore imagebox size are changing so the border must be redrawn.
            RedrawBorder();
        }
        private void BtnShrink_Click(object sender, EventArgs e)
        {
            //Shrinks the size of the window and PictureBox
            Size -= sizeChange;
            ImgBox.Size -= sizeChange;
            
            //Moves the cursor so that it stays in the same place over the Shrink button
            Cursor.Position = new Point(Cursor.Position.X, Cursor.Position.Y - sizeChangeAmount);

            //The window size and therefore imagebox size are changing so the border must be redrawn.
            RedrawBorder();
        }
    }
}


