/*
 * A290CalculatorMain.cs
 * 
 * This is the main Form of the A290 Calculator
 * 
 * Author: Sean Goldthwaite
 * Date Created: 11/30/2020
 * Last Modified by: Sean Goldthwaite
 * Date Last Modified: 12/2/2020
 * Assignment: Homework Assignment 3
 */

using System;
using System.Windows.Forms;

namespace A290Calculator
{
    public partial class A290CalculatorMain : Form
    {
        //doubles for first input, second input, and output
        private double inputA;
        private double inputB;
        private double answer;

        //booleans tracking if the inputs have been properly entered or not
        private bool inputAEntered = false;
        private bool inputBEntered = false;

        public A290CalculatorMain()
        {
            InitializeComponent();
        }

        private void TxtInputA_Leave(object sender, EventArgs e)
        {
            //If nothing has been entered, sets boolean as such but does not alert the user
            if (TxtInputA.Text == "")
            {
                inputAEntered = false;
            }
            //If the text successfully parses to a double, the double is automatically stored in inputA (through 'out' variable) and the associated boolean is then changed;
            else if (double.TryParse(TxtInputA.Text.ToString(), out inputA))
            {
                inputAEntered = true;
            }
            else //text entered was not a number
            {
                //Empties the text box and alerts the user of their mistake
                TxtInputA.Text = "";
                TxtInputA.Lines.Initialize();
                MessageBox.Show("Please enter a number");
                TxtInputA.Focus();
            }
        }

        private void TxtInputB_Leave(object sender, EventArgs e)
        {
            //If nothing has been entered, sets boolean as such but does not alert the user
            if (TxtInputB.Text == "")
            {
                inputBEntered = false;
            }
            //If the text successfully parses to a double, the double is automatically stored in inputB (through 'out' variable) and the associated boolean is then changed;
            else if (double.TryParse(TxtInputB.Text.ToString(), out inputB))
            {
                inputBEntered = true;
            }
            else //text entered was not a number
            {
                //Empties the text box and alerts the user of their mistake
                TxtInputB.Text = "";
                TxtInputB.Lines.Initialize();
                MessageBox.Show("Please enter a number");
                TxtInputB.Focus();
            }
        }

        private void btnPlus_Click(object sender, EventArgs e)
        {
            if (inputAEntered && inputBEntered)
            {
                //adds the inputs and displays them to 10 decimal places
                answer = inputA + inputB;
                TxtAnswer.Text = answer.ToString("#.##########");
            }
            else
            {
                MessageBox.Show("Both inputs must be entered");
            }
        }

        private void BtnMinus_Click(object sender, EventArgs e)
        {
            if (inputAEntered && inputBEntered)
            {
                //subtracts the inputs and displays them to 10 decimal places
                answer = inputA - inputB;
                TxtAnswer.Text = answer.ToString("#.##########");
            }
            else
            {
                MessageBox.Show("Both inputs must be entered");
            }
        }

        private void BtnMultiply_Click(object sender, EventArgs e)
        {
            if (inputAEntered && inputBEntered)
            {
                //multiplies the inputs and displays them to 10 decimal places
                answer = inputA * inputB;
                TxtAnswer.Text = answer.ToString("#.##########");
            }
            else
            {
                MessageBox.Show("Both inputs must be entered");
            }
        }

        private void BtnDivide_Click(object sender, EventArgs e)
        {
            if (inputAEntered && inputBEntered)
            {
                //If user is dividing by 0
                if (inputB == 0)
                {
                    //changes the answer box text to reflect that
                    TxtAnswer.Text = "Divide by 0 error";
                }
                else
                {
                    //divides the inputs and displays them to 10 decimal places
                    answer = inputA / inputB;
                    TxtAnswer.Text = answer.ToString("#.##########");
                }
                
            }
            else
            {
                MessageBox.Show("Both inputs must be entered");
            }
        }

        private void BtnSquare_Click(object sender, EventArgs e)
        {
            if (inputAEntered)
            {
                //squares input A and displays it to 10 decimal places
                answer = inputA * inputA;
                TxtAnswer.Text = answer.ToString("#.##########");
            }
            else
            {
                MessageBox.Show("Input A must be entered");
            }
        }

        private void BtnSqrt_Click(object sender, EventArgs e)
        {
            if (inputAEntered)
            {
                //square roots input A and displays it to 10 decimal places
                answer = Math.Sqrt(inputA);
                TxtAnswer.Text = answer.ToString("#.##########");
            }
            else
            {
                MessageBox.Show("Input A must be entered");
            }
        }

        private void BtnExponent_Click(object sender, EventArgs e)
        {
            if (inputAEntered && inputBEntered)
            {
                //raises input a to the b-th power
                answer = Math.Pow(inputA, inputB);

                //if answer would display as infinity
                if (answer.Equals(double.PositiveInfinity))
                    //displays "overflow error" instead
                    TxtAnswer.Text = "overflow error";
                else
                    //properly displays result to 10 decimal places
                    TxtAnswer.Text = answer.ToString("#.##########");
            }
            else
            {
                MessageBox.Show("Both inputs must be entered");
            }
        }

        private void BtnLogarithm_Click(object sender, EventArgs e)
        {
            if (inputAEntered && inputBEntered)
            {
                //takes log base b of a and displays it to 10 decimal places
                answer = Math.Log(inputB) / Math.Log(inputA);
                TxtAnswer.Text = answer.ToString("#.##########");
            }
            else
            {
                MessageBox.Show("Both inputs must be entered");
            }
        }

        private void BtnClear_Click(object sender, EventArgs e)
        {
            //Empties all text boxes
            TxtInputA.Text = "";
            TxtInputB.Text = "";
            TxtAnswer.Text = "";

            //signals that no inputs have yet been entered
            inputAEntered = false;
            inputBEntered = false;
        }

        private void BtnQuit_Click(object sender, EventArgs e)
        {
            //immediately closes the form
            Close();
        }
    }
}
