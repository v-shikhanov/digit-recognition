# Digit Recognition

Implemented simplest self-learning neural network for recognition digits handwritten on pictures.
Program uses two collections of images: 60000 pics learn collection and 10000 pics test collection.
So learning process going independent from checking network work.
Collection contains 28*28 pixel image, where every pixel is shade of grey from 0 to 255 where 0- white, 255 -black. Also every image has a label associated to it, which describes what digit is on image. 

### Self-learning modes
* Delta algorithm 
* Backpropagation algorithm

### What program could do
* Try to recognize digit on random image from test collection
* Check prediction accuracy on test collection 
* Self learn using learn collection. Network could be learned by Delta or Backpropagation algorithm
