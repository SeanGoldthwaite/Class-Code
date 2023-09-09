using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Data;
using System.Drawing;
using System.Text;
using System.Windows.Forms;
using System.IO;
using System.Linq;
using CsvHelper;
using System.Dynamic;
using System.Globalization;

namespace B365TxtParser
{
    public partial class ParserMain : Form
    {
        private List<dynamic> records = new List<dynamic>();

        public ParserMain()
        {
            InitializeComponent();
        }

        private void BtnSelectFolder_Click(object sender, EventArgs e)
        {
            Dictionary<Char, int> characterFrequency = new Dictionary<Char, int>();

            using (var fbd = new FolderBrowserDialog())
            {
                DialogResult result = fbd.ShowDialog();

                if (result == DialogResult.OK && !string.IsNullOrWhiteSpace(fbd.SelectedPath))
                {
                    String[] directories = Directory.GetDirectories(fbd.SelectedPath);

                    foreach (string directory in directories)
                    {
                        string[] files = Directory.GetFiles(directory);

                        string label = directory.Substring(directory.Length - 2);

                        foreach (string file in files)
                        {
                            dynamic record = new ExpandoObject();
                            List<object> stats = new List<object>();
                            List<int> wordCounts = new List<int>();

                            using (StreamReader sr = new StreamReader(file))
                            {
                                while (sr.Peek() >= 0)
                                {
                                    string line = sr.ReadLine();
                                    string[] words = line.Split();
                                    foreach (string word in words)
                                    {
                                        wordCounts.Add(word.Length);
                                        foreach (Char ch in word.ToLower())
                                        {
                                            if (characterFrequency.ContainsKey(ch))
                                                characterFrequency[ch] += 1;
                                            else
                                                characterFrequency[ch] = 1;
                                        }
                                    }
                                }
                            }
                            char maxKey = 'a';
                            int maxValue = int.MinValue;

                            foreach (KeyValuePair<Char, int> pair in characterFrequency)
                            {
                                if (pair.Value > maxValue)
                                {
                                    maxKey = pair.Key;
                                    maxValue = pair.Value;
                                }
                            }

                            record.avgLen = wordCounts.Average();
                            record.stDev = CalculateStandardDeviation(wordCounts);
                            record.mcc = maxKey;

                            //Apostrophes per word
                            if (characterFrequency.ContainsKey('\''))
                                record.apoFreq = (double)characterFrequency['\''] / wordCounts.Count;
                            else
                                record.apoFreq = 0;

                            record.label = label;;

                            records.Add(record);
                            characterFrequency.Clear();
                        }
                    }
                }
            }

            BtnSelectFolder.Visible = false;
            BtnSelectFolder.Enabled = false;

            BtnStoreData.Visible = true;
            BtnStoreData.Enabled = true;

            BackColor = Color.LawnGreen;
            LblSuccess.Visible = true;
        }
        private double CalculateStandardDeviation(IEnumerable<int> values)
        {
            double avg = values.Average();
            return Math.Sqrt(values.Average(v => Math.Pow((v - avg), 2)));
        }

        private void BtnStoreData_Click(object sender, EventArgs e)
        {
            using (var fbd = new FolderBrowserDialog())
            {
                DialogResult result = fbd.ShowDialog();

                if (result == DialogResult.OK && !string.IsNullOrWhiteSpace(fbd.SelectedPath))
                {
                    try
                    {
                        using (var writer = new StreamWriter(fbd.SelectedPath + "\\data.csv"))
                        using (var csv = new CsvWriter(writer, CultureInfo.InvariantCulture))
                        {
                            
                            csv.WriteRecords(records);
                            BackColor = Color.LawnGreen;
                            LblSuccess.Text = ".csv sucessfully created";

                            BtnStoreData.Visible = false;
                            BtnStoreData.Enabled = false;

                            BtnExit.Visible = true;
                            BtnExit.Enabled = true;
                        }
                    }
                    catch (Exception)
                    {
                        BackColor = Color.OrangeRed;
                        LblSuccess.Text = "There was an issue, try again";
                    }
                }
            }
        }

        private void BtnExit_Click(object sender, EventArgs e)
        {
            Close();
        }
    }
}
