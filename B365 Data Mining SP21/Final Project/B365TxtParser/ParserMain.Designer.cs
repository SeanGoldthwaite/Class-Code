namespace B365TxtParser
{
    partial class ParserMain
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
            this.FbdSelectFolder = new System.Windows.Forms.FolderBrowserDialog();
            this.BtnSelectFolder = new System.Windows.Forms.Button();
            this.BtnStoreData = new System.Windows.Forms.Button();
            this.LblSuccess = new System.Windows.Forms.Label();
            this.BtnExit = new System.Windows.Forms.Button();
            this.SuspendLayout();
            // 
            // BtnSelectFolder
            // 
            this.BtnSelectFolder.Location = new System.Drawing.Point(42, 67);
            this.BtnSelectFolder.Name = "BtnSelectFolder";
            this.BtnSelectFolder.Size = new System.Drawing.Size(100, 50);
            this.BtnSelectFolder.TabIndex = 0;
            this.BtnSelectFolder.Text = "Select Data Folder";
            this.BtnSelectFolder.UseVisualStyleBackColor = true;
            this.BtnSelectFolder.Click += new System.EventHandler(this.BtnSelectFolder_Click);
            // 
            // BtnStoreData
            // 
            this.BtnStoreData.Enabled = false;
            this.BtnStoreData.Location = new System.Drawing.Point(42, 67);
            this.BtnStoreData.Name = "BtnStoreData";
            this.BtnStoreData.Size = new System.Drawing.Size(100, 50);
            this.BtnStoreData.TabIndex = 1;
            this.BtnStoreData.Text = "Select folder for .csv";
            this.BtnStoreData.UseVisualStyleBackColor = true;
            this.BtnStoreData.Visible = false;
            this.BtnStoreData.Click += new System.EventHandler(this.BtnStoreData_Click);
            // 
            // LblSuccess
            // 
            this.LblSuccess.AutoSize = true;
            this.LblSuccess.Location = new System.Drawing.Point(25, 25);
            this.LblSuccess.Name = "LblSuccess";
            this.LblSuccess.Size = new System.Drawing.Size(136, 15);
            this.LblSuccess.TabIndex = 2;
            this.LblSuccess.Text = "Data Successfully Parsed";
            this.LblSuccess.Visible = false;
            // 
            // BtnExit
            // 
            this.BtnExit.Enabled = false;
            this.BtnExit.Location = new System.Drawing.Point(42, 67);
            this.BtnExit.Name = "BtnExit";
            this.BtnExit.Size = new System.Drawing.Size(100, 50);
            this.BtnExit.TabIndex = 1;
            this.BtnExit.Text = "Exit";
            this.BtnExit.UseVisualStyleBackColor = true;
            this.BtnExit.Visible = false;
            this.BtnExit.Click += new System.EventHandler(this.BtnExit_Click);
            // 
            // ParserMain
            // 
            this.AutoScaleDimensions = new System.Drawing.SizeF(7F, 15F);
            this.AutoScaleMode = System.Windows.Forms.AutoScaleMode.Font;
            this.ClientSize = new System.Drawing.Size(184, 161);
            this.Controls.Add(this.LblSuccess);
            this.Controls.Add(this.BtnStoreData);
            this.Controls.Add(this.BtnSelectFolder);
            this.Controls.Add(this.BtnExit);
            this.Name = "ParserMain";
            this.Text = "B365 Final Project Txt Parser";
            this.ResumeLayout(false);
            this.PerformLayout();

        }

        #endregion

        private System.Windows.Forms.FolderBrowserDialog FbdSelectFolder;
        private System.Windows.Forms.Button BtnSelectFolder;
        private System.Windows.Forms.Button BtnStoreData;
        private System.Windows.Forms.Label LblSuccess;
        private System.Windows.Forms.Button BtnExit;
    }
}