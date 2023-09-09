/*
 * FrmBuffetOptions.cs
 * 
 * This file is related to the Collections form of the A290 Buffet Main
 * 
 * Author: Sean Goldthwaite
 * Date Created: 11/10/2020
 * Last Modified by: Sean Goldthwaite
 * Date Last Modified: 11/17/2020
 * Assignment: Homework Assignment 2
 */

using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Data;
using System.Drawing;
using System.Text;
using System.Windows.Forms;

namespace Homework_Project_1
{
    public partial class FrmBuffetCollections : Form
    {
        public FrmBuffetCollections()
        {
            InitializeComponent();

            //Changing one property on 3 of the Collections form's controls
            this.BtnRedChannelToggle.BackColor = Color.Red;
            this.BtnGreenChannelToggle.BackColor = Color.Green;
            this.BtnBlueChannelToggle.BackColor = Color.Blue;
        }

        private void FrmCollectionsApply_Click(object sender, EventArgs e)
        {
            //Gracefully exits the form
            Close();
        }

        private void BtnCancel_Click(object sender, EventArgs e)
        {
            //Gracefully exits the form
            Close();
        }

        private void BtnShowControlNames_Click(object sender, EventArgs e)
        {
            //Loops through the Controls of the form and prints their index and Name property in individual MessageBoxes
            for (int i = 0; i < Controls.Count; i++)
            {
                MessageBox.Show("Control #" + i + " has the name " + Controls[i].Name);
            }
        }
    }
}
