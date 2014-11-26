MooBox
======

MooBox for pi

This project is just for fun...

It consist of controling servo motors with buttons to make Moo Box turning and triggering the famous 
sound moooooooooooooooo.

GPIO 00 (pin 17) manage servo 1
GPIO 01 (pin 18) manage servo 2

pin 1 (3.3v) is used to send a signal to GPIO 03 and 04 

GPIO 03 (pin 22) and 04 (pin 23) are set as Resistance.poll_down and are listening the input provided by pin 1

pin 2 is used to provide power to servo
pin 6 is used as ground


![map](http://jserviceswordpress.wordpress-hebergement.fr//wp-content/uploads/sites/2841/2014/04/pins_thumb.png "map")

A 2300 ma power supply is used to power everything


/!\ [Pi4J](http://pi4j.com/) is needed /!\

