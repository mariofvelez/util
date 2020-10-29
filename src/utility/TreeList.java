package utility;

public class TreeList <T> {
	TreeItem<T> begin;
	TreeItem<T> end;
	
	public TreeList()
	{
		
	}
	public void add(T item)
	{
		TreeItem<T> tree_item = new TreeItem<>(item);
		if(begin == null)
		{
			begin = tree_item;
			end = tree_item;
		}
		else
		{
			end.next = tree_item;
			tree_item.prev = end;
			end = tree_item;
		}
	}
	public void remove(T item)
	{
		
	}
	public void print()
	{
		for(TreeItem<T> item = begin; item != null; item = item.next)
			System.out.println(item.item);
	}
	public static void main(String[] args)
	{
		TreeList<Integer> list = new TreeList<>();
		for(int i = 0; i < 5; i++)
			list.add(i*2 + 1);
		list.print();
	}
}
class TreeItem <T> {
	T item;
	TreeItem<T> next;
	TreeItem<T> prev;
	
	public TreeItem(T item)
	{
		this.item = item;
	}
	public TreeItem(TreeItem<T> prev)
	{
		
	}
}
