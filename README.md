MooBox
======

MooBox for pi

This project is just for fun...

It consist of controling servo motors with buttons to make Moo Box turning and triggering the famous 
sound moooooooooooooooo.

GPIO 00 (pin 17) manage servo 1

GPIO 01 (pin 18) manage servo 2

GPIO 02 (pin 20) manage the shutdown of the raspberry pi (the signal must be connected to the ground)

GPIO 03 (pin 22) and 04 (pin 23) are set as pull_up and are listening a pull_down event

pin 2 is used to provide power to servo

pin 6 is used as ground for servos and button


![map](http://jserviceswordpress.wordpress-hebergement.fr//wp-content/uploads/sites/2841/2014/04/pins_thumb.png "map")

A 2100 ma power supply is used to power everything


/!\ [Pi4J](http://pi4j.com/) is needed /!\

