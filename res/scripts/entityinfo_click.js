//Toggle the entity info panel

importClass(Packages.gui.GUI);
importClass(Packages.gui.Panel);

var info = GUI.getElement("mnu_entityinfo");

info.setVisible(!info.isVisible());