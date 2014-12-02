//Toggle the entity info panel

importClass(Packages.gui.GUI);
importClass(Packages.gui.Panel);

var info = GUI.getElement("pnl_entityinfo");

info.setVisible(!info.isVisible());