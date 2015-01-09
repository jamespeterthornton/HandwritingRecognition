HandwritingRecognition
=======================

This is a java implementation of the basic handwriting recognition algorithm presented (in Python) in chapters one and two of Michael Nielsen's "Neural Networks and Deep Learning," available here: http://neuralnetworksanddeeplearning.com/

The algorithm simulates a feedforward neural network that uses stochastic gradient descent and backpropogation to classify handwritten digits from the MNIST dataset. 

To see it in action, clone the repo and run the file "Network.java."

In general, this was a learner project intended as the first step in teaching myself how to build neural nets. Placing the emphasis on my own instruction, I sought to reference the original code as few times as possible. I did not use any outside matrix libraries, but instead implemented all of the matrix mathematics myself in order to be certain that I fully understood them. One interesting property of this network - which may be due to having manually implemented the matrix mathematics functions - is that it is around five or six times as fast as the original code.

-James