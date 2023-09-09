/*
 * A290CalculatorMain.Designer.cs
 * 
 * This is the Designer form associated with the main Form of the A290 Calculator
 * 
 * Author: Sean Goldthwaite
 * Date Created: 11/30/2020
 * Last Modified by: Sean Goldthwaite
 * Date Last Modified: 12/2/2020
 * Assignment: Homework Assignment 3
 */

using System.Windows;

namespace A290Calculator
{
    partial class A290CalculatorMain
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
            this.LblInputA = new System.Windows.Forms.Label();
            this.LblInputB = new System.Windows.Forms.Label();
            this.TxtInputA = new System.Windows.Forms.TextBox();
            this.TxtInputB = new System.Windows.Forms.TextBox();
            this.BtnPlus = new System.Windows.Forms.Button();
            this.BtnMinus = new System.Windows.Forms.Button();
            this.BtnMultiply = new System.Windows.Forms.Button();
            this.BtnDivide = new System.Windows.Forms.Button();
            this.BtnLogarithm = new System.Windows.Forms.Button();
            this.BtnExponent = new System.Windows.Forms.Button();
            this.BtnSqrt = new System.Windows.Forms.Button();
            this.BtnSquare = new System.Windows.Forms.Button();
            this.LblAnswer = new System.Windows.Forms.Label();
            this.TxtAnswer = new System.Windows.Forms.TextBox();
            this.BtnClear = new System.Windows.Forms.Button();
            this.BtnQuit = new System.Windows.Forms.Button();
            this.SuspendLayout();
            // 
            // LblInputA
            // 
            this.LblInputA.AutoSize = true;
            this.LblInputA.Location = new System.Drawing.Point(15, 15);
            this.LblInputA.Name = "LblInputA";
            this.LblInputA.Size = new System.Drawing.Size(46, 15);
            this.LblInputA.TabIndex = 15;
            this.LblInputA.Text = "Input A";
            // 
            // LblInputB
            // 
            this.LblInputB.AutoSize = true;
            this.LblInputB.Location = new System.Drawing.Point(15, 160);
            this.LblInputB.Name = "LblInputB";
            this.LblInputB.Size = new System.Drawing.Size(45, 15);
            this.LblInputB.TabIndex = 14;
            this.LblInputB.Text = "Input B";
            // 
            // TxtInputA
            // 
            this.TxtInputA.BackColor = System.Drawing.Color.FromArgb(((int)(((byte)(50)))), ((int)(((byte)(130)))), ((int)(((byte)(184)))));
            this.TxtInputA.BorderStyle = System.Windows.Forms.BorderStyle.FixedSingle;
            this.TxtInputA.ForeColor = System.Drawing.Color.FromArgb(((int)(((byte)(187)))), ((int)(((byte)(225)))), ((int)(((byte)(250)))));
            this.TxtInputA.Location = new System.Drawing.Point(15, 33);
            this.TxtInputA.Name = "TxtInputA";
            this.TxtInputA.Size = new System.Drawing.Size(100, 23);
            this.TxtInputA.TabIndex = 0;
            this.TxtInputA.Leave += new System.EventHandler(this.TxtInputA_Leave);
            // 
            // TxtInputB
            // 
            this.TxtInputB.BackColor = System.Drawing.Color.FromArgb(((int)(((byte)(50)))), ((int)(((byte)(130)))), ((int)(((byte)(184)))));
            this.TxtInputB.BorderStyle = System.Windows.Forms.BorderStyle.FixedSingle;
            this.TxtInputB.ForeColor = System.Drawing.Color.FromArgb(((int)(((byte)(187)))), ((int)(((byte)(225)))), ((int)(((byte)(250)))));
            this.TxtInputB.Location = new System.Drawing.Point(15, 178);
            this.TxtInputB.Name = "TxtInputB";
            this.TxtInputB.Size = new System.Drawing.Size(100, 23);
            this.TxtInputB.TabIndex = 1;
            this.TxtInputB.Leave += new System.EventHandler(this.TxtInputB_Leave);
            // 
            // BtnPlus
            // 
            this.BtnPlus.BackColor = System.Drawing.Color.FromArgb(((int)(((byte)(15)))), ((int)(((byte)(76)))), ((int)(((byte)(117)))));
            this.BtnPlus.FlatStyle = System.Windows.Forms.FlatStyle.Popup;
            this.BtnPlus.Font = new System.Drawing.Font("Segoe UI", 14.25F, System.Drawing.FontStyle.Regular, System.Drawing.GraphicsUnit.Point);
            this.BtnPlus.ForeColor = System.Drawing.Color.FromArgb(((int)(((byte)(187)))), ((int)(((byte)(225)))), ((int)(((byte)(250)))));
            this.BtnPlus.Location = new System.Drawing.Point(215, 33);
            this.BtnPlus.Name = "BtnPlus";
            this.BtnPlus.Size = new System.Drawing.Size(50, 30);
            this.BtnPlus.TabIndex = 2;
            this.BtnPlus.Text = "+";
            this.BtnPlus.UseVisualStyleBackColor = false;
            this.BtnPlus.Click += new System.EventHandler(this.btnPlus_Click);
            // 
            // BtnMinus
            // 
            this.BtnMinus.BackColor = System.Drawing.Color.FromArgb(((int)(((byte)(15)))), ((int)(((byte)(76)))), ((int)(((byte)(117)))));
            this.BtnMinus.FlatStyle = System.Windows.Forms.FlatStyle.Popup;
            this.BtnMinus.Font = new System.Drawing.Font("Segoe UI", 14.25F, System.Drawing.FontStyle.Regular, System.Drawing.GraphicsUnit.Point);
            this.BtnMinus.ForeColor = System.Drawing.Color.FromArgb(((int)(((byte)(187)))), ((int)(((byte)(225)))), ((int)(((byte)(250)))));
            this.BtnMinus.Location = new System.Drawing.Point(215, 79);
            this.BtnMinus.Name = "BtnMinus";
            this.BtnMinus.Size = new System.Drawing.Size(50, 30);
            this.BtnMinus.TabIndex = 3;
            this.BtnMinus.Text = "-";
            this.BtnMinus.UseVisualStyleBackColor = false;
            this.BtnMinus.Click += new System.EventHandler(this.BtnMinus_Click);
            // 
            // BtnMultiply
            // 
            this.BtnMultiply.BackColor = System.Drawing.Color.FromArgb(((int)(((byte)(15)))), ((int)(((byte)(76)))), ((int)(((byte)(117)))));
            this.BtnMultiply.FlatStyle = System.Windows.Forms.FlatStyle.Popup;
            this.BtnMultiply.Font = new System.Drawing.Font("Segoe UI", 14.25F, System.Drawing.FontStyle.Regular, System.Drawing.GraphicsUnit.Point);
            this.BtnMultiply.ForeColor = System.Drawing.Color.FromArgb(((int)(((byte)(187)))), ((int)(((byte)(225)))), ((int)(((byte)(250)))));
            this.BtnMultiply.Location = new System.Drawing.Point(215, 125);
            this.BtnMultiply.Name = "BtnMultiply";
            this.BtnMultiply.Size = new System.Drawing.Size(50, 30);
            this.BtnMultiply.TabIndex = 4;
            this.BtnMultiply.Text = "*";
            this.BtnMultiply.UseVisualStyleBackColor = false;
            this.BtnMultiply.Click += new System.EventHandler(this.BtnMultiply_Click);
            // 
            // BtnDivide
            // 
            this.BtnDivide.BackColor = System.Drawing.Color.FromArgb(((int)(((byte)(15)))), ((int)(((byte)(76)))), ((int)(((byte)(117)))));
            this.BtnDivide.FlatStyle = System.Windows.Forms.FlatStyle.Popup;
            this.BtnDivide.Font = new System.Drawing.Font("Segoe UI", 14.25F, System.Drawing.FontStyle.Regular, System.Drawing.GraphicsUnit.Point);
            this.BtnDivide.ForeColor = System.Drawing.Color.FromArgb(((int)(((byte)(187)))), ((int)(((byte)(225)))), ((int)(((byte)(250)))));
            this.BtnDivide.Location = new System.Drawing.Point(215, 170);
            this.BtnDivide.Name = "BtnDivide";
            this.BtnDivide.Size = new System.Drawing.Size(50, 30);
            this.BtnDivide.TabIndex = 5;
            this.BtnDivide.Text = "/";
            this.BtnDivide.UseVisualStyleBackColor = false;
            this.BtnDivide.Click += new System.EventHandler(this.BtnDivide_Click);
            // 
            // BtnLogarithm
            // 
            this.BtnLogarithm.BackColor = System.Drawing.Color.FromArgb(((int)(((byte)(15)))), ((int)(((byte)(76)))), ((int)(((byte)(117)))));
            this.BtnLogarithm.FlatStyle = System.Windows.Forms.FlatStyle.Popup;
            this.BtnLogarithm.ForeColor = System.Drawing.Color.FromArgb(((int)(((byte)(187)))), ((int)(((byte)(225)))), ((int)(((byte)(250)))));
            this.BtnLogarithm.Location = new System.Drawing.Point(271, 171);
            this.BtnLogarithm.Name = "BtnLogarithm";
            this.BtnLogarithm.Size = new System.Drawing.Size(50, 30);
            this.BtnLogarithm.TabIndex = 9;
            this.BtnLogarithm.Text = "LogₐB";
            this.BtnLogarithm.UseVisualStyleBackColor = false;
            this.BtnLogarithm.Click += new System.EventHandler(this.BtnLogarithm_Click);
            // 
            // BtnExponent
            // 
            this.BtnExponent.BackColor = System.Drawing.Color.FromArgb(((int)(((byte)(15)))), ((int)(((byte)(76)))), ((int)(((byte)(117)))));
            this.BtnExponent.FlatStyle = System.Windows.Forms.FlatStyle.Popup;
            this.BtnExponent.Font = new System.Drawing.Font("Segoe UI", 14.25F, System.Drawing.FontStyle.Regular, System.Drawing.GraphicsUnit.Point);
            this.BtnExponent.ForeColor = System.Drawing.Color.FromArgb(((int)(((byte)(187)))), ((int)(((byte)(225)))), ((int)(((byte)(250)))));
            this.BtnExponent.Location = new System.Drawing.Point(271, 125);
            this.BtnExponent.Name = "BtnExponent";
            this.BtnExponent.Size = new System.Drawing.Size(50, 30);
            this.BtnExponent.TabIndex = 8;
            this.BtnExponent.Text = "Aᴮ";
            this.BtnExponent.UseVisualStyleBackColor = false;
            this.BtnExponent.Click += new System.EventHandler(this.BtnExponent_Click);
            // 
            // BtnSqrt
            // 
            this.BtnSqrt.BackColor = System.Drawing.Color.FromArgb(((int)(((byte)(15)))), ((int)(((byte)(76)))), ((int)(((byte)(117)))));
            this.BtnSqrt.FlatStyle = System.Windows.Forms.FlatStyle.Popup;
            this.BtnSqrt.Font = new System.Drawing.Font("Segoe UI", 11.25F, System.Drawing.FontStyle.Regular, System.Drawing.GraphicsUnit.Point);
            this.BtnSqrt.ForeColor = System.Drawing.Color.FromArgb(((int)(((byte)(187)))), ((int)(((byte)(225)))), ((int)(((byte)(250)))));
            this.BtnSqrt.Location = new System.Drawing.Point(271, 79);
            this.BtnSqrt.Name = "BtnSqrt";
            this.BtnSqrt.Size = new System.Drawing.Size(50, 30);
            this.BtnSqrt.TabIndex = 7;
            this.BtnSqrt.Text = "√A";
            this.BtnSqrt.UseVisualStyleBackColor = false;
            this.BtnSqrt.Click += new System.EventHandler(this.BtnSqrt_Click);
            // 
            // BtnSquare
            // 
            this.BtnSquare.BackColor = System.Drawing.Color.FromArgb(((int)(((byte)(15)))), ((int)(((byte)(76)))), ((int)(((byte)(117)))));
            this.BtnSquare.FlatStyle = System.Windows.Forms.FlatStyle.Popup;
            this.BtnSquare.Font = new System.Drawing.Font("Segoe UI", 11.25F, System.Drawing.FontStyle.Regular, System.Drawing.GraphicsUnit.Point);
            this.BtnSquare.ForeColor = System.Drawing.Color.FromArgb(((int)(((byte)(187)))), ((int)(((byte)(225)))), ((int)(((byte)(250)))));
            this.BtnSquare.Location = new System.Drawing.Point(271, 33);
            this.BtnSquare.Name = "BtnSquare";
            this.BtnSquare.Size = new System.Drawing.Size(50, 30);
            this.BtnSquare.TabIndex = 6;
            this.BtnSquare.Text = "A²";
            this.BtnSquare.UseVisualStyleBackColor = false;
            this.BtnSquare.Click += new System.EventHandler(this.BtnSquare_Click);
            // 
            // LblAnswer
            // 
            this.LblAnswer.AutoSize = true;
            this.LblAnswer.Location = new System.Drawing.Point(501, 98);
            this.LblAnswer.Name = "LblAnswer";
            this.LblAnswer.Size = new System.Drawing.Size(45, 15);
            this.LblAnswer.TabIndex = 13;
            this.LblAnswer.Text = "Output";
            // 
            // TxtAnswer
            // 
            this.TxtAnswer.BackColor = System.Drawing.Color.FromArgb(((int)(((byte)(50)))), ((int)(((byte)(130)))), ((int)(((byte)(184)))));
            this.TxtAnswer.BorderStyle = System.Windows.Forms.BorderStyle.FixedSingle;
            this.TxtAnswer.ForeColor = System.Drawing.Color.FromArgb(((int)(((byte)(187)))), ((int)(((byte)(225)))), ((int)(((byte)(250)))));
            this.TxtAnswer.Location = new System.Drawing.Point(376, 116);
            this.TxtAnswer.Name = "TxtAnswer";
            this.TxtAnswer.ReadOnly = true;
            this.TxtAnswer.Size = new System.Drawing.Size(175, 23);
            this.TxtAnswer.TabIndex = 12;
            // 
            // BtnClear
            // 
            this.BtnClear.BackColor = System.Drawing.Color.FromArgb(((int)(((byte)(15)))), ((int)(((byte)(76)))), ((int)(((byte)(117)))));
            this.BtnClear.FlatStyle = System.Windows.Forms.FlatStyle.Popup;
            this.BtnClear.ForeColor = System.Drawing.Color.FromArgb(((int)(((byte)(187)))), ((int)(((byte)(225)))), ((int)(((byte)(250)))));
            this.BtnClear.Location = new System.Drawing.Point(501, 33);
            this.BtnClear.Name = "BtnClear";
            this.BtnClear.Size = new System.Drawing.Size(50, 30);
            this.BtnClear.TabIndex = 10;
            this.BtnClear.Text = "Clear";
            this.BtnClear.UseVisualStyleBackColor = false;
            this.BtnClear.Click += new System.EventHandler(this.BtnClear_Click);
            // 
            // BtnQuit
            // 
            this.BtnQuit.BackColor = System.Drawing.Color.FromArgb(((int)(((byte)(15)))), ((int)(((byte)(76)))), ((int)(((byte)(117)))));
            this.BtnQuit.FlatStyle = System.Windows.Forms.FlatStyle.Popup;
            this.BtnQuit.ForeColor = System.Drawing.Color.FromArgb(((int)(((byte)(187)))), ((int)(((byte)(225)))), ((int)(((byte)(250)))));
            this.BtnQuit.Location = new System.Drawing.Point(501, 171);
            this.BtnQuit.Name = "BtnQuit";
            this.BtnQuit.Size = new System.Drawing.Size(50, 30);
            this.BtnQuit.TabIndex = 11;
            this.BtnQuit.Text = "Quit";
            this.BtnQuit.UseVisualStyleBackColor = false;
            this.BtnQuit.Click += new System.EventHandler(this.BtnQuit_Click);
            // 
            // A290CalculatorMain
            // 
            this.AutoScaleDimensions = new System.Drawing.SizeF(7F, 15F);
            this.AutoScaleMode = System.Windows.Forms.AutoScaleMode.Font;
            this.BackColor = System.Drawing.Color.FromArgb(((int)(((byte)(27)))), ((int)(((byte)(38)))), ((int)(((byte)(44)))));
            this.CancelButton = this.BtnClear;
            this.ClientSize = new System.Drawing.Size(584, 236);
            this.Controls.Add(this.BtnQuit);
            this.Controls.Add(this.BtnClear);
            this.Controls.Add(this.TxtAnswer);
            this.Controls.Add(this.LblAnswer);
            this.Controls.Add(this.BtnSquare);
            this.Controls.Add(this.BtnSqrt);
            this.Controls.Add(this.BtnExponent);
            this.Controls.Add(this.BtnLogarithm);
            this.Controls.Add(this.BtnDivide);
            this.Controls.Add(this.BtnMultiply);
            this.Controls.Add(this.BtnMinus);
            this.Controls.Add(this.BtnPlus);
            this.Controls.Add(this.TxtInputB);
            this.Controls.Add(this.TxtInputA);
            this.Controls.Add(this.LblInputB);
            this.Controls.Add(this.LblInputA);
            this.ForeColor = System.Drawing.Color.FromArgb(((int)(((byte)(187)))), ((int)(((byte)(225)))), ((int)(((byte)(250)))));
            this.FormBorderStyle = System.Windows.Forms.FormBorderStyle.FixedDialog;
            this.MaximumSize = new System.Drawing.Size(600, 275);
            this.MinimumSize = new System.Drawing.Size(600, 275);
            this.Name = "A290CalculatorMain";
            this.ShowIcon = false;
            this.StartPosition = System.Windows.Forms.FormStartPosition.CenterScreen;
            this.Text = "A290 Calculator";
            this.ResumeLayout(false);
            this.PerformLayout();

        }

        #endregion

        private System.Windows.Forms.Label LblInputA;
        private System.Windows.Forms.Label LblInputB;
        private System.Windows.Forms.TextBox TxtInputA;
        private System.Windows.Forms.TextBox TxtInputB;
        private System.Windows.Forms.Button BtnPlus;
        private System.Windows.Forms.Button BtnMinus;
        private System.Windows.Forms.Button BtnMultiply;
        private System.Windows.Forms.Button BtnDivide;
        private System.Windows.Forms.Button BtnLogarithm;
        private System.Windows.Forms.Button BtnExponent;
        private System.Windows.Forms.Button BtnSqrt;
        private System.Windows.Forms.Button BtnSquare;
        private System.Windows.Forms.Label LblAnswer;
        private System.Windows.Forms.TextBox TxtAnswer;
        private System.Windows.Forms.Button BtnClear;
        private System.Windows.Forms.Button BtnQuit;
    }
}

