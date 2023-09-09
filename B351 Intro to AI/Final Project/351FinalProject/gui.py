from tkinter import *
from PIL import Image, ImageGrab
import win32gui
import NeuralNetwork as nn
import HDRFullyConnected as HD

master = Tk()
master.geometry('500x500')
master.title('Digit Recognizer')


w = Canvas(width=700, height=300, bg='black')
w.pack()


# clears out the current drawing
def clear():
    w.delete('all')

# allows you to draw by holding your left mouse button or using your touch
# screen if you have it


def paint(mouse):
    x1 = mouse.x
    y1 = mouse.y
    x2 = mouse.x
    y2 = mouse.y
    w.create_line(x1, y1, x2, y2, width=30, fill="white",
                  capstyle=ROUND,splinesteps=36)


'''
UsesWin32Gui & Pil in order to grab the drawn picture then reshapes to the
picture to fit into the parameters set by the neural network. After reshaping
it runs into the guess function of neural network to produce an output
'''


def calculate():

    lgrab = w.winfo_id()
    location = win32gui.GetWindowRect(lgrab)
    im = ImageGrab.grab(location)
    im.save('file.png')
    img = Image.open('file.png').convert('L')

    img = img.resize((28, 28))
    '''
    img =  Image.open('file.png').convert('L')
    #img.save('resized.png')
    '''

    neuralNet = nn.NeuralNetwork(784, 100, 100, 10, 0.001, True)
    guess = HD.predictCustom(neuralNet, img)


w.bind('<B1-Motion>', paint)

btn1 = Button(master, text='Calculate', width=25, bd='5', command=calculate)
btn1.pack(side='bottom')


btn2 = Button(master, text='Clear', width=25, bd='5', command=clear)
btn2.pack(side='bottom')
mainloop()
