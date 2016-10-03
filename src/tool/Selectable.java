package tool;

/**
 * Created by Nyrmburk on 10/2/2016.
 */
public interface Selectable {

	default void onSelect() {}
	default void onDeselect(){}
	default void onSelectionChanged(Selection selection){}
}
