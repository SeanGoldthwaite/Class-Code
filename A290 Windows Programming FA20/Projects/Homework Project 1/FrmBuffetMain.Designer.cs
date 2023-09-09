/*
 * FrmBuffetMain.Designer.cs
 * 
 * This file is a part of the Main form of the A290 Buffet and instanciates all of the objects and their properties/fields on the Main form.
 * 
 * Author: Sean Goldthwaite
 * Date Created: 11/04/2020
 * Last Modified by: Sean Goldthwaite
 * Date Last Modified: 11/17/2020
 * Assignment: Homework Assignment 2
 */

namespace Homework_Project_1
{
    partial class FrmBuffetMain
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
            this.BtnQuit = new System.Windows.Forms.Button();
            this.BtnSelectImage = new System.Windows.Forms.Button();
            this.ImgBox = new System.Windows.Forms.PictureBox();
            this.BtnDrawBorder = new System.Windows.Forms.Button();
            this.OfdSelectPicture = new System.Windows.Forms.OpenFileDialog();
            this.BtnHideBorder = new System.Windows.Forms.Button();
            this.LblX = new System.Windows.Forms.Label();
            this.LblY = new System.Windows.Forms.Label();
            this.BtnEnlarge = new System.Windows.Forms.Button();
            this.BtnShrink = new System.Windows.Forms.Button();
            this.BtnOptions = new System.Windows.Forms.Button();
            this.LblImgDim = new System.Windows.Forms.Label();
            this.BtnCollections = new System.Windows.Forms.Button();
            this.ChkPromptOnExit = new System.Windows.Forms.CheckBox();
            ((System.ComponentModel.ISupportInitialize)(this.ImgBox)).BeginInit();
            this.SuspendLayout();
            // 
            // BtnQuit
            // 
            this.BtnQuit.Anchor = ((System.Windows.Forms.AnchorStyles)((System.Windows.Forms.AnchorStyles.Bottom | System.Windows.Forms.AnchorStyles.Left)));
            this.BtnQuit.Location = new System.Drawing.Point(25, 515);
            this.BtnQuit.Name = "BtnQuit";
            this.BtnQuit.Size = new System.Drawing.Size(100, 25);
            this.BtnQuit.TabIndex = 1;
            this.BtnQuit.Text = "Quit";
            this.BtnQuit.UseVisualStyleBackColor = true;
            this.BtnQuit.Click += new System.EventHandler(this.BtnQuit_Click);
            // 
            // BtnSelectImage
            // 
            this.BtnSelectImage.Anchor = ((System.Windows.Forms.AnchorStyles)((System.Windows.Forms.AnchorStyles.Bottom | System.Windows.Forms.AnchorStyles.Left)));
            this.BtnSelectImage.Location = new System.Drawing.Point(25, 485);
            this.BtnSelectImage.Name = "BtnSelectImage";
            this.BtnSelectImage.Size = new System.Drawing.Size(100, 25);
            this.BtnSelectImage.TabIndex = 0;
            this.BtnSelectImage.Text = "Select Image";
            this.BtnSelectImage.UseVisualStyleBackColor = true;
            this.BtnSelectImage.Click += new System.EventHandler(this.BtnSelectImage_Click);
            // 
            // ImgBox
            // 
            this.ImgBox.BorderStyle = System.Windows.Forms.BorderStyle.FixedSingle;
            this.ImgBox.Location = new System.Drawing.Point(25, 25);
            this.ImgBox.Name = "ImgBox";
            this.ImgBox.Size = new System.Drawing.Size(725, 450);
            this.ImgBox.SizeMode = System.Windows.Forms.PictureBoxSizeMode.StretchImage;
            this.ImgBox.TabIndex = 2;
            this.ImgBox.TabStop = false;
            this.ImgBox.MouseLeave += new System.EventHandler(this.ImgBox_MouseLeave);
            this.ImgBox.MouseMove += new System.Windows.Forms.MouseEventHandler(this.ImgBox_MouseMove);
            // 
            // BtnDrawBorder
            // 
            this.BtnDrawBorder.Anchor = ((System.Windows.Forms.AnchorStyles)((System.Windows.Forms.AnchorStyles.Bottom | System.Windows.Forms.AnchorStyles.Left)));
            this.BtnDrawBorder.Location = new System.Drawing.Point(135, 485);
            this.BtnDrawBorder.Name = "BtnDrawBorder";
            this.BtnDrawBorder.Size = new System.Drawing.Size(100, 25);
            this.BtnDrawBorder.TabIndex = 3;
            this.BtnDrawBorder.Text = "Draw Border";
            this.BtnDrawBorder.UseVisualStyleBackColor = true;
            this.BtnDrawBorder.Click += new System.EventHandler(this.BtnDrawBorder_Click);
            // 
            // OfdSelectPicture
            // 
            this.OfdSelectPicture.Filter = "PNG files (*.PNG)|*.PNG|jpg files (*.jpg)|*.jpg|gif files (*.gif)|*.gif|All Files" +
    " (*.*)|*.*";
            this.OfdSelectPicture.Title = "Select Picture";
            // 
            // BtnHideBorder
            // 
            this.BtnHideBorder.Anchor = ((System.Windows.Forms.AnchorStyles)((System.Windows.Forms.AnchorStyles.Bottom | System.Windows.Forms.AnchorStyles.Left)));
            this.BtnHideBorder.Location = new System.Drawing.Point(135, 515);
            this.BtnHideBorder.Name = "BtnHideBorder";
            this.BtnHideBorder.Size = new System.Drawing.Size(100, 25);
            this.BtnHideBorder.TabIndex = 5;
            this.BtnHideBorder.Text = "Hide Border";
            this.BtnHideBorder.UseVisualStyleBackColor = true;
            this.BtnHideBorder.Click += new System.EventHandler(this.BtnHideBorder_Click);
            // 
            // LblX
            // 
            this.LblX.Anchor = ((System.Windows.Forms.AnchorStyles)((System.Windows.Forms.AnchorStyles.Bottom | System.Windows.Forms.AnchorStyles.Left)));
            this.LblX.AutoSize = true;
            this.LblX.Location = new System.Drawing.Point(650, 485);
            this.LblX.Name = "LblX";
            this.LblX.Size = new System.Drawing.Size(20, 15);
            this.LblX.TabIndex = 6;
            this.LblX.Text = "X: ";
            // 
            // LblY
            // 
            this.LblY.Anchor = ((System.Windows.Forms.AnchorStyles)((System.Windows.Forms.AnchorStyles.Bottom | System.Windows.Forms.AnchorStyles.Left)));
            this.LblY.AutoSize = true;
            this.LblY.Location = new System.Drawing.Point(650, 515);
            this.LblY.Name = "LblY";
            this.LblY.Size = new System.Drawing.Size(23, 15);
            this.LblY.TabIndex = 6;
            this.LblY.Text = "Y:  ";
            // 
            // BtnEnlarge
            // 
            this.BtnEnlarge.Anchor = ((System.Windows.Forms.AnchorStyles)((System.Windows.Forms.AnchorStyles.Bottom | System.Windows.Forms.AnchorStyles.Left)));
            this.BtnEnlarge.Location = new System.Drawing.Point(245, 485);
            this.BtnEnlarge.Name = "BtnEnlarge";
            this.BtnEnlarge.Size = new System.Drawing.Size(100, 25);
            this.BtnEnlarge.TabIndex = 6;
            this.BtnEnlarge.Text = "Enlarge";
            this.BtnEnlarge.UseVisualStyleBackColor = true;
            this.BtnEnlarge.Click += new System.EventHandler(this.BtnEnlarge_Click);
            // 
            // BtnShrink
            // 
            this.BtnShrink.Anchor = ((System.Windows.Forms.AnchorStyles)((System.Windows.Forms.AnchorStyles.Bottom | System.Windows.Forms.AnchorStyles.Left)));
            this.BtnShrink.Location = new System.Drawing.Point(245, 515);
            this.BtnShrink.Name = "BtnShrink";
            this.BtnShrink.Size = new System.Drawing.Size(100, 25);
            this.BtnShrink.TabIndex = 7;
            this.BtnShrink.Text = "Shrink";
            this.BtnShrink.UseVisualStyleBackColor = true;
            this.BtnShrink.Click += new System.EventHandler(this.BtnShrink_Click);
            // 
            // BtnOptions
            // 
            this.BtnOptions.Anchor = ((System.Windows.Forms.AnchorStyles)((System.Windows.Forms.AnchorStyles.Bottom | System.Windows.Forms.AnchorStyles.Left)));
            this.BtnOptions.Location = new System.Drawing.Point(355, 515);
            this.BtnOptions.Name = "BtnOptions";
            this.BtnOptions.Size = new System.Drawing.Size(100, 25);
            this.BtnOptions.TabIndex = 9;
            this.BtnOptions.Text = "Options";
            this.BtnOptions.UseVisualStyleBackColor = true;
            this.BtnOptions.Click += new System.EventHandler(this.BtnOptions_Click);
            // 
            // LblImgDim
            // 
            this.LblImgDim.Anchor = ((System.Windows.Forms.AnchorStyles)((System.Windows.Forms.AnchorStyles.Bottom | System.Windows.Forms.AnchorStyles.Left)));
            this.LblImgDim.AutoSize = true;
            this.LblImgDim.Location = new System.Drawing.Point(465, 485);
            this.LblImgDim.Name = "LblImgDim";
            this.LblImgDim.Size = new System.Drawing.Size(111, 15);
            this.LblImgDim.TabIndex = 9;
            this.LblImgDim.Text = "Image Dimensions: ";
            // 
            // BtnCollections
            // 
            this.BtnCollections.Anchor = ((System.Windows.Forms.AnchorStyles)((System.Windows.Forms.AnchorStyles.Bottom | System.Windows.Forms.AnchorStyles.Left)));
            this.BtnCollections.Location = new System.Drawing.Point(355, 485);
            this.BtnCollections.Name = "BtnCollections";
            this.BtnCollections.Size = new System.Drawing.Size(100, 25);
            this.BtnCollections.TabIndex = 8;
            this.BtnCollections.Text = "Collections";
            this.BtnCollections.UseVisualStyleBackColor = true;
            this.BtnCollections.Click += new System.EventHandler(this.BtnCollections_Click);
            // 
            // ChkPromptOnExit
            // 
            this.ChkPromptOnExit.Anchor = ((System.Windows.Forms.AnchorStyles)((System.Windows.Forms.AnchorStyles.Bottom | System.Windows.Forms.AnchorStyles.Left)));
            this.ChkPromptOnExit.AutoSize = true;
            this.ChkPromptOnExit.CheckAlign = System.Drawing.ContentAlignment.MiddleRight;
            this.ChkPromptOnExit.Location = new System.Drawing.Point(465, 515);
            this.ChkPromptOnExit.Name = "ChkPromptOnExit";
            this.ChkPromptOnExit.Size = new System.Drawing.Size(108, 19);
            this.ChkPromptOnExit.TabIndex = 10;
            this.ChkPromptOnExit.Text = "Prompt on Exit:";
            this.ChkPromptOnExit.UseVisualStyleBackColor = true;
            // 
            // FrmBuffetMain
            // 
            this.AutoScaleDimensions = new System.Drawing.SizeF(7F, 15F);
            this.AutoScaleMode = System.Windows.Forms.AutoScaleMode.Font;
            this.ClientSize = new System.Drawing.Size(784, 561);
            this.Controls.Add(this.ChkPromptOnExit);
            this.Controls.Add(this.BtnCollections);
            this.Controls.Add(this.LblImgDim);
            this.Controls.Add(this.BtnOptions);
            this.Controls.Add(this.BtnShrink);
            this.Controls.Add(this.BtnEnlarge);
            this.Controls.Add(this.LblY);
            this.Controls.Add(this.LblX);
            this.Controls.Add(this.BtnHideBorder);
            this.Controls.Add(this.BtnDrawBorder);
            this.Controls.Add(this.ImgBox);
            this.Controls.Add(this.BtnSelectImage);
            this.Controls.Add(this.BtnQuit);
            this.ForeColor = System.Drawing.SystemColors.ControlText;
            this.MinimumSize = new System.Drawing.Size(720, 520);
            this.Name = "FrmBuffetMain";
            this.Text = "A290 Buffet";
            this.ResizeEnd += new System.EventHandler(this.FrmBuffetMain_ResizeEnd);
            ((System.ComponentModel.ISupportInitialize)(this.ImgBox)).EndInit();
            this.ResumeLayout(false);
            this.PerformLayout();

        }

        #endregion

        private System.Windows.Forms.Button BtnQuit;
        private System.Windows.Forms.Button BtnSelectImage;
        private System.Windows.Forms.PictureBox ImgBox;
        private System.Windows.Forms.Button BtnDrawBorder;
        private System.Windows.Forms.OpenFileDialog OfdSelectPicture;
        private System.Windows.Forms.Button BtnHideBorder;
        private System.Windows.Forms.Label LblX;
        private System.Windows.Forms.Label LblY;
        private System.Windows.Forms.Button BtnEnlarge;
        private System.Windows.Forms.Button BtnShrink;
        private System.Windows.Forms.Button BtnOptions;
        private System.Windows.Forms.Label LblImgDim;
        private System.Windows.Forms.Button BtnCollections;
        private System.Windows.Forms.CheckBox ChkPromptOnExit;
    }
}

