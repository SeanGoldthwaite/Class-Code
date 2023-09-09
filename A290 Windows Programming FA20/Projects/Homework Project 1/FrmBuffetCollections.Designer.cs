/*
 * FrmBuffetCollections.Designer.cs
 * 
 * This file is a part of the FrmBuffetCollections form of the A290 Buffet and instanciates all of the objects and their properties/fields on the FrmBuffetCollections form.
 * 
 * Author: Sean Goldthwaite
 * Date Created: 11/10/2020
 * Last Modified by: Sean Goldthwaite
 * Date Last Modified: 11/17/2020
 * Assignment: Homework Assignment 2
 */

namespace Homework_Project_1
{
    partial class FrmBuffetCollections
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
            this.BtnApply = new System.Windows.Forms.Button();
            this.BtnCancel = new System.Windows.Forms.Button();
            this.BtnShowControlNames = new System.Windows.Forms.Button();
            this.BtnMirrorHorizontal = new System.Windows.Forms.Button();
            this.BtnRedChannelToggle = new System.Windows.Forms.Button();
            this.BtnMirrorVertical = new System.Windows.Forms.Button();
            this.BtnBlueChannelToggle = new System.Windows.Forms.Button();
            this.BtnGreenChannelToggle = new System.Windows.Forms.Button();
            this.BtnInvertImage = new System.Windows.Forms.Button();
            this.LblColorControls = new System.Windows.Forms.Label();
            this.LblImageMirroring = new System.Windows.Forms.Label();
            this.SuspendLayout();
            // 
            // BtnApply
            // 
            //Required property change for Collections form controls
            this.BtnApply.FlatStyle = System.Windows.Forms.FlatStyle.Popup;

            this.BtnApply.Location = new System.Drawing.Point(85, 325);
            this.BtnApply.Name = "BtnApply";
            this.BtnApply.Size = new System.Drawing.Size(100, 25);
            this.BtnApply.TabIndex = 7;
            this.BtnApply.Text = "Apply";
            this.BtnApply.UseVisualStyleBackColor = true;
            this.BtnApply.Click += new System.EventHandler(this.FrmCollectionsApply_Click);
            // 
            // BtnCancel
            // 
            //Required property change for Collections form controls
            this.BtnCancel.FlatStyle = System.Windows.Forms.FlatStyle.Popup;

            this.BtnCancel.Location = new System.Drawing.Point(200, 325);
            this.BtnCancel.Name = "BtnCancel";
            this.BtnCancel.Size = new System.Drawing.Size(100, 25);
            this.BtnCancel.TabIndex = 8;
            this.BtnCancel.Text = "Cancel";
            this.BtnCancel.UseVisualStyleBackColor = true;
            this.BtnCancel.Click += new System.EventHandler(this.BtnCancel_Click);
            // 
            // BtnShowControlNames
            // 
            //Required property change for Collections form controls
            this.BtnShowControlNames.FlatStyle = System.Windows.Forms.FlatStyle.Popup;

            this.BtnShowControlNames.Location = new System.Drawing.Point(85, 295);
            this.BtnShowControlNames.Name = "BtnShowControlNames";
            this.BtnShowControlNames.Size = new System.Drawing.Size(215, 25);
            this.BtnShowControlNames.TabIndex = 6;
            this.BtnShowControlNames.Text = "Show Control Names";
            this.BtnShowControlNames.UseVisualStyleBackColor = true;
            this.BtnShowControlNames.Click += new System.EventHandler(this.BtnShowControlNames_Click);
            // 
            // BtnMirrorHorizontal
            // 
            //Required property change for Collections form controls
            this.BtnMirrorHorizontal.FlatStyle = System.Windows.Forms.FlatStyle.Popup;

            this.BtnMirrorHorizontal.Location = new System.Drawing.Point(85, 200);
            this.BtnMirrorHorizontal.Name = "BtnMirrorHorizontal";
            this.BtnMirrorHorizontal.Size = new System.Drawing.Size(100, 40);
            this.BtnMirrorHorizontal.TabIndex = 4;
            this.BtnMirrorHorizontal.Text = "Mirror Horizontally";
            this.BtnMirrorHorizontal.UseVisualStyleBackColor = false;
            // 
            // BtnRedChannelToggle
            // 
            this.BtnRedChannelToggle.ImageKey = "(none)";
            this.BtnRedChannelToggle.Location = new System.Drawing.Point(36, 80);
            this.BtnRedChannelToggle.Name = "BtnRedChannelToggle";
            this.BtnRedChannelToggle.Size = new System.Drawing.Size(100, 40);
            this.BtnRedChannelToggle.TabIndex = 0;
            this.BtnRedChannelToggle.Text = "Red Channel On/Off";
            this.BtnRedChannelToggle.UseVisualStyleBackColor = true;
            // 
            // BtnMirrorVertical
            // 
            //Required property change for Collections form controls
            this.BtnMirrorVertical.FlatStyle = System.Windows.Forms.FlatStyle.Popup;

            this.BtnMirrorVertical.Location = new System.Drawing.Point(200, 200);
            this.BtnMirrorVertical.Name = "BtnMirrorVertical";
            this.BtnMirrorVertical.Size = new System.Drawing.Size(100, 40);
            this.BtnMirrorVertical.TabIndex = 5;
            this.BtnMirrorVertical.Text = "Mirror  Vertically";
            this.BtnMirrorVertical.UseVisualStyleBackColor = true;
            // 
            // BtnBlueChannelToggle
            // 
            this.BtnBlueChannelToggle.Location = new System.Drawing.Point(248, 80);
            this.BtnBlueChannelToggle.Name = "BtnBlueChannelToggle";
            this.BtnBlueChannelToggle.Size = new System.Drawing.Size(100, 40);
            this.BtnBlueChannelToggle.TabIndex = 2;
            this.BtnBlueChannelToggle.Text = "Blue Channel On/Off";
            this.BtnBlueChannelToggle.UseVisualStyleBackColor = true;
            // 
            // BtnGreenChannelToggle
            // 
            this.BtnGreenChannelToggle.Location = new System.Drawing.Point(142, 80);
            this.BtnGreenChannelToggle.Name = "BtnGreenChannelToggle";
            this.BtnGreenChannelToggle.Size = new System.Drawing.Size(100, 40);
            this.BtnGreenChannelToggle.TabIndex = 1;
            this.BtnGreenChannelToggle.Text = "Green Channel On/Off";
            this.BtnGreenChannelToggle.UseVisualStyleBackColor = true;
            // 
            // BtnInvertImage
            // 
            //Required property change for Collections form controls
            this.BtnInvertImage.BackColor = System.Drawing.SystemColors.ControlText;
            this.BtnInvertImage.ForeColor = System.Drawing.SystemColors.Control;

            this.BtnInvertImage.Location = new System.Drawing.Point(85, 125);
            this.BtnInvertImage.Name = "BtnInvertImage";
            this.BtnInvertImage.Size = new System.Drawing.Size(206, 25);
            this.BtnInvertImage.TabIndex = 3;
            this.BtnInvertImage.Text = "Invert Image";
            this.BtnInvertImage.UseVisualStyleBackColor = false;
            // 
            // LblColorControls
            // 
            this.LblColorControls.Font = new System.Drawing.Font("Segoe UI", 9F, System.Drawing.FontStyle.Bold, System.Drawing.GraphicsUnit.Point);
            this.LblColorControls.Location = new System.Drawing.Point(142, 60);
            this.LblColorControls.Name = "LblColorControls";
            this.LblColorControls.Size = new System.Drawing.Size(100, 15);
            this.LblColorControls.TabIndex = 1;
            this.LblColorControls.Text = "Color Controls";
            this.LblColorControls.TextAlign = System.Drawing.ContentAlignment.MiddleCenter;
            // 
            // LblImageMirroring
            // 
            this.LblImageMirroring.Font = new System.Drawing.Font("Segoe UI", 9F, System.Drawing.FontStyle.Bold, System.Drawing.GraphicsUnit.Point);
            this.LblImageMirroring.Location = new System.Drawing.Point(85, 180);
            this.LblImageMirroring.Name = "LblImageMirroring";
            this.LblImageMirroring.Size = new System.Drawing.Size(215, 15);
            this.LblImageMirroring.TabIndex = 0;
            this.LblImageMirroring.Text = "Image Mirroring/Flipping";
            this.LblImageMirroring.TextAlign = System.Drawing.ContentAlignment.MiddleCenter;
            // 
            // FrmBuffetCollections
            // 
            this.AcceptButton = this.BtnApply;
            this.AutoScaleDimensions = new System.Drawing.SizeF(7F, 15F);
            this.AutoScaleMode = System.Windows.Forms.AutoScaleMode.Font;
            this.BackColor = System.Drawing.SystemColors.ControlLight;
            this.CancelButton = this.BtnCancel;
            this.ClientSize = new System.Drawing.Size(384, 361);
            this.Controls.Add(this.LblImageMirroring);
            this.Controls.Add(this.LblColorControls);
            this.Controls.Add(this.BtnCancel);
            this.Controls.Add(this.BtnApply);
            this.Controls.Add(this.BtnMirrorVertical);
            this.Controls.Add(this.BtnMirrorHorizontal);
            this.Controls.Add(this.BtnInvertImage);
            this.Controls.Add(this.BtnBlueChannelToggle);
            this.Controls.Add(this.BtnGreenChannelToggle);
            this.Controls.Add(this.BtnRedChannelToggle);
            this.Controls.Add(this.BtnShowControlNames);
            this.ForeColor = System.Drawing.SystemColors.ControlText;
            this.FormBorderStyle = System.Windows.Forms.FormBorderStyle.FixedToolWindow;
            this.MaximizeBox = false;
            this.MaximumSize = new System.Drawing.Size(400, 400);
            this.MinimizeBox = false;
            this.MinimumSize = new System.Drawing.Size(400, 400);
            this.Name = "FrmBuffetCollections";
            this.ShowIcon = false;
            this.ShowInTaskbar = false;
            this.SizeGripStyle = System.Windows.Forms.SizeGripStyle.Hide;
            this.StartPosition = System.Windows.Forms.FormStartPosition.CenterParent;
            this.Text = "A290 Buffet Collection";
            this.ResumeLayout(false);

        }

        #endregion

        private System.Windows.Forms.Button BtnApply;
        private System.Windows.Forms.Button BtnCancel;
        private System.Windows.Forms.Button BtnShowControlNames;
        private System.Windows.Forms.Button BtnMirrorHorizontal;
        private System.Windows.Forms.Button BtnRedChannelToggle;
        private System.Windows.Forms.Button BtnMirrorVertical;
        private System.Windows.Forms.Button BtnBlueChannelToggle;
        private System.Windows.Forms.Button BtnGreenChannelToggle;
        private System.Windows.Forms.Button BtnInvertImage;
        private System.Windows.Forms.Label LblColorControls;
        private System.Windows.Forms.Label LblImageMirroring;
    }
}