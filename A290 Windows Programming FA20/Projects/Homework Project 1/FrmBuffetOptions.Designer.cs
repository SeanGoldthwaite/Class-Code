/*
 * FrmBuffetOptions.Designer.cs
 * 
 * This file is a part of the Options form of the A290 Buffet and instanciates all of the objects and their properties/fields on the Main form.
 * 
 * Author: Sean Goldthwaite
 * Date Created: 11/04/2020
 * Last Modified by: Sean Goldthwaite
 * Date Last Modified: 11/17/2020
 * Assignment: Homework Assignment 2
 */

namespace Homework_Project_1
{
    partial class FrmBuffetOptions
    {
        /// <summary>
        /// Required designer variable.
        /// </summary>
        private System.ComponentModel.IContainer components = null;

        /// <summary>
        /// Clean up any resources being used.
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
        /// Required method for Designer support - do not modify
        /// the contents of this method with the code editor.
        /// </summary>
        private void InitializeComponent()
        {
            this.BtnOptionsCancel = new System.Windows.Forms.Button();
            this.BtnOptionsApply = new System.Windows.Forms.Button();
            this.DpdBorderColorImage = new System.Windows.Forms.ComboBox();
            this.LblUsername = new System.Windows.Forms.Label();
            this.TxtUsername = new System.Windows.Forms.TextBox();
            this.GbxBackgroundColor = new System.Windows.Forms.GroupBox();
            this.RdbDefault = new System.Windows.Forms.RadioButton();
            this.RdbGreen = new System.Windows.Forms.RadioButton();
            this.RdbBlue = new System.Windows.Forms.RadioButton();
            this.RdbRed = new System.Windows.Forms.RadioButton();
            this.DpdBorderColorWindow = new System.Windows.Forms.ComboBox();
            this.LblImageBorderColor = new System.Windows.Forms.Label();
            this.LblWindowBorderColor = new System.Windows.Forms.Label();
            this.GbxBackgroundColor.SuspendLayout();
            this.SuspendLayout();
            // 
            // BtnOptionsApply
            // 
            this.BtnOptionsApply.Location = new System.Drawing.Point(85, 325);
            this.BtnOptionsApply.Name = "BtnOptionsApply";
            this.BtnOptionsApply.Size = new System.Drawing.Size(100, 25);
            this.BtnOptionsApply.TabIndex = 0;
            this.BtnOptionsApply.Text = "Apply";
            this.BtnOptionsApply.Click += new System.EventHandler(this.BtnOptionsApply_Click);
            // 
            // BtnOptionsCancel
            // 
            this.BtnOptionsCancel.Location = new System.Drawing.Point(200, 325);
            this.BtnOptionsCancel.Name = "BtnOptionsCancel";
            this.BtnOptionsCancel.Size = new System.Drawing.Size(100, 25);
            this.BtnOptionsCancel.TabIndex = 1;
            this.BtnOptionsCancel.Text = "Cancel";
            this.BtnOptionsCancel.Click += new System.EventHandler(this.BtnOptionsCancel_Click);
            // 
            // TxtUsername
            // 
            this.TxtUsername.Location = new System.Drawing.Point(10, 50);
            this.TxtUsername.Multiline = true;
            this.TxtUsername.Name = "TxtUsername";
            this.TxtUsername.PlaceholderText = "Username";
            this.TxtUsername.Size = new System.Drawing.Size(175, 100);
            this.TxtUsername.TabIndex = 2;
            // 
            // DpdBorderColorImage
            // 
            this.DpdBorderColorImage.DropDownHeight = 200;
            this.DpdBorderColorImage.DropDownStyle = System.Windows.Forms.ComboBoxStyle.DropDownList;
            this.DpdBorderColorImage.FormattingEnabled = true;
            this.DpdBorderColorImage.IntegralHeight = false;
            this.DpdBorderColorImage.Items.AddRange(new object[] {
            "Black",
            "Brown",
            "Purple",
            "Blue",
            "Cyan",
            "Green",
            "Yellow",
            "Gold",
            "Orange",
            "Red",
            "Crimson",
            "Silver",
            "Gray"});
            this.DpdBorderColorImage.Location = new System.Drawing.Point(200, 50);
            this.DpdBorderColorImage.Name = "DpdBorderColorImage";
            this.DpdBorderColorImage.Size = new System.Drawing.Size(125, 23);
            this.DpdBorderColorImage.TabIndex = 3;
            this.DpdBorderColorImage.SelectedIndexChanged += new System.EventHandler(this.DpdBorderColorImage_SelectedIndexChanged);
            // 
            // DpdBorderColorWindow
            // 
            this.DpdBorderColorWindow.DropDownHeight = 150;
            this.DpdBorderColorWindow.DropDownStyle = System.Windows.Forms.ComboBoxStyle.DropDownList;
            this.DpdBorderColorWindow.FormattingEnabled = true;
            this.DpdBorderColorWindow.IntegralHeight = false;
            this.DpdBorderColorWindow.Items.AddRange(new object[] {
            "Default",
            "Red",
            "Green",
            "Blue",
            "Black"});
            this.DpdBorderColorWindow.Location = new System.Drawing.Point(200, 127);
            this.DpdBorderColorWindow.Name = "DpdBorderColorWindow";
            this.DpdBorderColorWindow.Size = new System.Drawing.Size(125, 23);
            this.DpdBorderColorWindow.TabIndex = 4;
            this.DpdBorderColorWindow.SelectedIndexChanged += new System.EventHandler(this.DpdBorderColorWindow_SelectedIndexChanged);
            // 
            // RdbRed
            // 
            this.RdbRed.AutoSize = true;
            this.RdbRed.Location = new System.Drawing.Point(7, 23);
            this.RdbRed.Name = "RdbRed";
            this.RdbRed.Size = new System.Drawing.Size(45, 19);
            this.RdbRed.TabIndex = 5;
            this.RdbRed.TabStop = true;
            this.RdbRed.Text = "Red";
            this.RdbRed.UseVisualStyleBackColor = true;
            this.RdbRed.CheckedChanged += new System.EventHandler(this.RdbRed_CheckedChanged);
            // 
            // RdbGreen
            // 
            this.RdbGreen.AutoSize = true;
            this.RdbGreen.Location = new System.Drawing.Point(75, 23);
            this.RdbGreen.Name = "RdbGreen";
            this.RdbGreen.Size = new System.Drawing.Size(56, 19);
            this.RdbGreen.TabIndex = 6;
            this.RdbGreen.TabStop = true;
            this.RdbGreen.Text = "Green";
            this.RdbGreen.UseVisualStyleBackColor = true;
            this.RdbGreen.CheckedChanged += new System.EventHandler(this.RdbGreen_CheckedChanged);
            // 
            // RdbBlue
            // 
            this.RdbBlue.AutoSize = true;
            this.RdbBlue.Location = new System.Drawing.Point(6, 60);
            this.RdbBlue.Name = "RdbBlue";
            this.RdbBlue.Size = new System.Drawing.Size(48, 19);
            this.RdbBlue.TabIndex = 7;
            this.RdbBlue.TabStop = true;
            this.RdbBlue.Text = "Blue";
            this.RdbBlue.UseVisualStyleBackColor = true;
            this.RdbBlue.CheckedChanged += new System.EventHandler(this.RdbBlue_CheckedChanged);
            // 
            // RdbDefault
            // 
            this.RdbDefault.AutoSize = true;
            this.RdbDefault.Location = new System.Drawing.Point(75, 60);
            this.RdbDefault.Name = "RdbDefault";
            this.RdbDefault.Size = new System.Drawing.Size(63, 19);
            this.RdbDefault.TabIndex = 8;
            this.RdbDefault.TabStop = true;
            this.RdbDefault.Text = "Default";
            this.RdbDefault.UseVisualStyleBackColor = true;
            this.RdbDefault.CheckedChanged += new System.EventHandler(this.RdbDefault_CheckedChanged);
            // 
            // GbxBackgroundColor
            // 
            this.GbxBackgroundColor.Controls.Add(this.RdbDefault);
            this.GbxBackgroundColor.Controls.Add(this.RdbGreen);
            this.GbxBackgroundColor.Controls.Add(this.RdbBlue);
            this.GbxBackgroundColor.Controls.Add(this.RdbRed);
            this.GbxBackgroundColor.Location = new System.Drawing.Point(10, 160);
            this.GbxBackgroundColor.Name = "GbxBackgroundColor";
            this.GbxBackgroundColor.Size = new System.Drawing.Size(175, 100);
            this.GbxBackgroundColor.TabStop = false;
            this.GbxBackgroundColor.Text = "Background Color";
            // 
            // LblUsername
            // 
            this.LblUsername.AutoSize = true;
            this.LblUsername.Location = new System.Drawing.Point(10, 32);
            this.LblUsername.Name = "LblUsername";
            this.LblUsername.Size = new System.Drawing.Size(66, 15);
            this.LblUsername.Text = "Username: ";
            // 
            // LblImageBorderColor
            // 
            this.LblImageBorderColor.AutoSize = true;
            this.LblImageBorderColor.Location = new System.Drawing.Point(200, 32);
            this.LblImageBorderColor.Name = "LblImageBorderColor";
            this.LblImageBorderColor.Size = new System.Drawing.Size(110, 15);
            this.LblImageBorderColor.Text = "Image Border Color";
            // 
            // LblWindowBorderColor
            // 
            this.LblWindowBorderColor.AutoSize = true;
            this.LblWindowBorderColor.Location = new System.Drawing.Point(200, 109);
            this.LblWindowBorderColor.Name = "LblWindowBorderColor";
            this.LblWindowBorderColor.Size = new System.Drawing.Size(121, 15);
            this.LblWindowBorderColor.Text = "Window Border Color";
            // 
            // FrmBuffetOptions
            // 
            this.AcceptButton = this.BtnOptionsApply;
            this.AutoScaleDimensions = new System.Drawing.SizeF(7F, 15F);
            this.AutoScaleMode = System.Windows.Forms.AutoScaleMode.Font;
            this.CancelButton = this.BtnOptionsCancel;
            this.ClientSize = new System.Drawing.Size(384, 361);
            this.Controls.Add(this.LblWindowBorderColor);
            this.Controls.Add(this.LblImageBorderColor);
            this.Controls.Add(this.LblUsername);
            this.Controls.Add(this.GbxBackgroundColor);
            this.Controls.Add(this.DpdBorderColorWindow);
            this.Controls.Add(this.DpdBorderColorImage);
            this.Controls.Add(this.TxtUsername);
            this.Controls.Add(this.BtnOptionsCancel);
            this.Controls.Add(this.BtnOptionsApply);
            this.FormBorderStyle = System.Windows.Forms.FormBorderStyle.FixedSingle;
            this.MaximizeBox = false;
            this.MaximumSize = new System.Drawing.Size(400, 400);
            this.MinimizeBox = false;
            this.MinimumSize = new System.Drawing.Size(400, 400);
            this.Name = "FrmBuffetOptions";
            this.ShowInTaskbar = false;
            this.StartPosition = System.Windows.Forms.FormStartPosition.CenterParent;
            this.Text = "A290 Buffet Options";
            this.TopMost = true;
            this.GbxBackgroundColor.ResumeLayout(false);
            this.GbxBackgroundColor.PerformLayout();
            this.ResumeLayout(false);
            this.PerformLayout();

        }

        #endregion

        private System.Windows.Forms.Button BtnOptionsCancel;
        private System.Windows.Forms.Button BtnOptionsApply;
        private System.Windows.Forms.ComboBox DpdBorderColorImage;
        private System.Windows.Forms.Label LblUsername;
        private System.Windows.Forms.TextBox TxtUsername;
        private System.Windows.Forms.GroupBox GbxBackgroundColor;
        private System.Windows.Forms.ComboBox DpdBorderColorWindow;
        private System.Windows.Forms.Label LblImageBorderColor;
        private System.Windows.Forms.Label LblWindowBorderColor;
        private System.Windows.Forms.RadioButton RdbRed;
        private System.Windows.Forms.RadioButton RdbGreen;
        private System.Windows.Forms.RadioButton RdbBlue;
        private System.Windows.Forms.RadioButton RdbDefault;
    }
}