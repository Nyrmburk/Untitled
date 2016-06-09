# Untitled
This is my game I am creating. I currently name is "Untitled" because it is about creating and because I don't have a better name. I have learned so much during my experimentation. I would say this is the biggest project I have ever worked on and has also been the most rewarding. While most of the progress is substantial, it is currently an iceberg with most of the progress invisible from the top.

Gameplay

I want this to be a 2D physics platformer with a powerful level editor. I want the editor to be so powerful that the game's goals and challenges change significantly from level to level. There will be the basic character controls expected from a platformer. I may add sliding or some others. Another movement action I think I will probably add is grabbing. Grabbing allows for the direct interaction with the physics nature of the environment. If I can think of any more movement related actions that allow for more interaction with the environment I will probably add them. Because the heart of this game is in the levels, I need to create a way to share them. I believe sharing and showing off are social aspects that will raise the popularity of the game.

Special Features

I have made the rendering engine only accessible through an interface which means that there is no implementation-specific code in the engine. This allows for an easy porting to Android by just writing a new renderer. It also means that if this ever gets big, a modder can create a "super graphics" mod without having to even change the engine. It also means that I can support multiple graphics api levels without much difficulty.

I have made the input system in a similar fashion. Because of the structure, most, if not all, inputs can be derived from all kinds of devices including mouse, controller, keyboard, and touchpad. This means it will be easy to set a binding map for different devices including Android devices without changing any code.

I have created a nearly complete layout engine for the GUI. It is structured similarly to AWT but is headless. Main differences from Swing include the (WIP) feature of loading from a file similarly to Android and allowing Javascript to be embedded into said file similar to HTML. This allows for tremendous flexibility in modifying the user interface as well as creating new ones. Custom levels may have their own user interface with level specific information. Another difference from Swing, is that it supports "Activities" similar to Android. This allows for more structured navigation through the interface.

Scripting is pretty simple thanks to Java having a Javascript engine being built in. Not only does it allow for interpreting Javascript, but it can be compiled into Java bytecode and ran natively.

For the physics engine, I decided to go with the JBox2D port of Box2D. In the future I may create my own fork of JBox2D to support multithreading for extra performance.
