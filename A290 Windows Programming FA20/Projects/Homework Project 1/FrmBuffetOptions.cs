/*
 * FrmBuffetOptions.cs
 * 
 * This file is related to the Options form of the A290 Buffet Main
 * 
 * Author: Sean Goldthwaite
 * Date Created: 11/04/2020
 * Last Modified by: Sean Goldthwaite
 * Date Last Modified: 11/17/2020
 * Assignment: Homework Assignment 2
 */

using System;
using System.Drawing;
using System.Windows.Forms;


namespace Homework_Project_1
{
    public partial class FrmBuffetOptions : Form
    {
        //Instance variable that stores the parent form
        //Readonly because it should not be chance after being initialized
        private readonly FrmBuffetMain Parent_Form;

        //Graphics object that draws the border of this window
        private Graphics BorderDraw;

        //Rectangle object that represents the border region.
        private Rectangle BorderRectangle = new Rectangle(1, 1, 382, 359);

        //Color of the border to be drawn
        private Pen BorderColor;

        public FrmBuffetOptions(FrmBuffetMain Parent_Form)
        {
            InitializeComponent();

            //Sets the Parent_Form instance variable
            this.Parent_Form = Parent_Form;

            //Initializes all content on this form to the values stored in the parent form
            TxtUsername.Text = this.Parent_Form.OptionsUsernameText;
            DpdBorderColorImage.Text = this.Parent_Form.OptionsImageBorderColorText;
            DpdBorderColorWindow.Text = this.Parent_Form.OptionsWindowBorderColorText;
            BorderColor = this.Parent_Form.OptionsBorderColor;

            switch (Parent_Form.OptionsBackgroundColor)
            {
                case FrmBuffetMain.RdbColorSelected.Default:
                    RdbDefault.Checked = true;
                    BackColor = DefaultBackColor;
                    break;
                case FrmBuffetMain.RdbColorSelected.Red:
                    RdbRed.Checked = true;
                    BackColor = Color.LightCoral;
                    break;
                case FrmBuffetMain.RdbColorSelected.Green:
                    RdbGreen.Checked = true;
                    BackColor = Color.SpringGreen;
                    break;
                case FrmBuffetMain.RdbColorSelected.Blue:
                    RdbBlue.Checked = true;
                    BackColor = Color.DodgerBlue;
                    break;
            }

            //Redraws the border with the specified color
            //Doesn't actually work, I'm not sure why. If there's something obvious I'm overlooking, please let me know
            RedrawBorder();
        }

        private void BtnOptionsCancel_Click(object sender, EventArgs e)
        {
            //Gracefully closes the options form without making any changes
            Close();
        }

        private void BtnOptionsApply_Click(object sender, EventArgs e)
        {
            //If the user is typing in the TextBox, "disables" ability to click apply so the "enter" key can be used.
            if (TxtUsername.Focused)
                return;

            //Sets the color of the pen in the parent from to the specified color
            switch (DpdBorderColorImage.SelectedIndex)
            {
                case 0:
                    Parent_Form.BorderPen = Pens.Black;
                    break;
                case 1:
                    Parent_Form.BorderPen = Pens.Brown;
                    break;
                case 2:
                    Parent_Form.BorderPen = Pens.Purple;
                    break;
                case 3:
                    Parent_Form.BorderPen = Pens.Blue;
                    break;
                case 4:
                    Parent_Form.BorderPen = Pens.Cyan;
                    break;
                case 5:
                    Parent_Form.BorderPen = Pens.Green;
                    break;
                case 6:
                    Parent_Form.BorderPen = Pens.Yellow;
                    break;
                case 7:
                    Parent_Form.BorderPen = Pens.Gold;
                    break;
                case 8:
                    Parent_Form.BorderPen = Pens.Orange;
                    break;
                case 9:
                    Parent_Form.BorderPen = Pens.Red;
                    break;
                case 10:
                    Parent_Form.BorderPen = Pens.Crimson;
                    break;
                case 11:
                    Parent_Form.BorderPen = Pens.Silver;
                    break;
                case 12:
                    Parent_Form.BorderPen = Pens.Gray;
                    break;
            }
            //Redraws the border in the parent form
            this.Parent_Form.RedrawBorder();

            //Checks which background color radio button is checked, and stores it in parent form with RdbColorSelected enum
            if (RdbDefault.Checked)
                Parent_Form.OptionsBackgroundColor = FrmBuffetMain.RdbColorSelected.Default;
            else if (RdbRed.Checked)
                Parent_Form.OptionsBackgroundColor = FrmBuffetMain.RdbColorSelected.Red;
            else if (RdbGreen.Checked)
                Parent_Form.OptionsBackgroundColor = FrmBuffetMain.RdbColorSelected.Green;
            else if (RdbBlue.Checked)
                Parent_Form.OptionsBackgroundColor = FrmBuffetMain.RdbColorSelected.Blue;

            //Updates the values in the parent form to the options selected so that they are the same when the options form is opened next
            Parent_Form.OptionsUsernameText = TxtUsername.Text;
            Parent_Form.OptionsWindowBorderColorText = DpdBorderColorWindow.Text;
            Parent_Form.OptionsImageBorderColorText = DpdBorderColorImage.Text;
            Parent_Form.OptionsBorderColor = BorderColor;


            //Gracefully exits the form after making all neccessary changes.
            Close();
        }

        private void DpdBorderColorImage_SelectedIndexChanged(object sender, EventArgs e)
        {
            //When the dropdown item is change, updates the text
            DpdBorderColorImage.Text = DpdBorderColorImage.SelectedItem.ToString();
        }
        public void DpdBorderColorWindow_SelectedIndexChanged(object sender, EventArgs e)
        {
            //Updates the BorderColor Pen object when a new item is selected
            switch (DpdBorderColorWindow.SelectedItem)
            {
                case "Default":
                    BorderColor = new Pen(DefaultBackColor, 2);
                    break;
                case "Red":
                    BorderColor = new Pen(Color.LightCoral, 2);
                    break;
                case "Green":
                    BorderColor = new Pen(Color.SpringGreen, 2);
                    break;
                case "Blue":
                    BorderColor = new Pen(Color.DodgerBlue, 2);
                    break;
                case "Black":
                    BorderColor = new Pen(Color.Black, 2);
                    break;
            }

            //Redraws the border with the new color
            RedrawBorder();
        }

        private void RdbDefault_CheckedChanged(object sender, EventArgs e)
        {
            //Changes background color to DefaultBackColor when default radio button is selected
            BackColor = DefaultBackColor;
        }
        private void RdbRed_CheckedChanged(object sender, EventArgs e)
        {
            //Changes background color to LightCoral when red radio button is selected
            BackColor = Color.LightCoral;
        }

        private void RdbGreen_CheckedChanged(object sender, EventArgs e)
        {
            //Changes background color to SpringGreen when green radio button is selected
            BackColor = Color.SpringGreen;
        }

        private void RdbBlue_CheckedChanged(object sender, EventArgs e)
        {
            //Changes background color to DodgerBlue when blue radio button is selected
            BackColor = Color.DodgerBlue;
        }

        private void RedrawBorder()
        {
            //Freshly redraws the border using the Bordercolor Pen object and BorderRectangle Rectangle
            BorderDraw = CreateGraphics();
            BorderDraw.DrawRectangle(BorderColor, BorderRectangle);
            BorderDraw.Dispose();
        }
    }
}
