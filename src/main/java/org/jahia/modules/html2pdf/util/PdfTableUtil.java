package org.jahia.modules.html2pdf.util;

import java.util.ArrayList;
import java.util.List;

import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;

/**
 * The Class PdfTableUtil.
 */
public class PdfTableUtil {
	
	/** The row list. */
	private List<String[]> rowList;

	/**
	 * Instantiates a new pdf table util.
	 *
	 * @param tableNode the table node
	 */
	public PdfTableUtil(Node tableNode) {
		rowList = new ArrayList<>();
		for (Element chElement : ((Element)tableNode).children()) {
			if(chElement.nodeName().equalsIgnoreCase("tbody") || 
			   chElement.nodeName().equalsIgnoreCase("thead")){
				for (Element schElement : chElement.children()) {
					String[] cells = getCellsFromNode(schElement);
					if(cells.length > 0)
						rowList.add(cells);
				}
			}else if(chElement.nodeName().equalsIgnoreCase("tr")){
				String[] cells = getCellsFromNode(chElement);
				if(cells.length > 0)
					rowList.add(cells);
			}
			
			
		}
	}	
	
	/**
	 * Gets the cells from node.
	 *
	 * @param cellElement the cell node
	 * @return the cells from node
	 */
	private String[] getCellsFromNode(Element cellElement){
		List<String> cellList = new ArrayList<String>();
		for (Element cell : cellElement.children()) {
			if(cell.nodeName().equalsIgnoreCase("td") ||
			   cell.nodeName().equalsIgnoreCase("th"))
				cellList.add(cell.text());
				//if(!cell.childNodes().isEmpty())
					//cellList.add(cell.childNode(0).toString());			
		}
		return convertList2Array(cellList);
	}
	
	/**
	 * Convert list2 array.
	 *
	 * @param list the list
	 * @return the string[]
	 */
	private String[] convertList2Array(List<String> list){
		String[] values = new String[list.size()];
		for (int i = 0; i < list.size(); i++) {
			values[i] = list.get(i) + "";
		}
		return values;
	}
	
	/**
	 * Gets the rows.
	 *
	 * @return the rows
	 */
	public List<String[]> getRows(){
		return this.rowList;
	}
	
	/**
	 * Gets the column size.
	 *
	 * @return the column size
	 */
	public Integer getColumnSize(){
		if(this.rowList.size() > 0){
			return this.rowList.get(0).length;
		}
		return 0;
	}
	
}
