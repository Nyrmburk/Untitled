importClass(Packages.main.Manager);
importClass(Packages.main.AssetManager);
importClass(Packages.entity.Item);
importClass(Packages.graphics.Model);

//use print to print text to the console
print('Hello, World!');
print('This is running from an external jsr 223 compliant language.');

//this statement defines a type of int[]
var intarray = Java.type("int[]");

//coord is a new intarray. It's argument is how long the array is
var coord = new intarray(2);

//seting the values
coord[0] = 12; coord[1] = 32;

//printing the values
print(coord + " = [" + coord[0] + ", " + coord[1] + "]");

//create a new model from the asset manager
var model = AssetManager.getModel("rocks.obj")

//create a new Item
var item = new Item("JSObject", Item.itemTypes.ROCK, coord, model);
print(item);

//add it to the update list
Manager.addEntity(item);
print(Manager.entityList.size() + " entities");