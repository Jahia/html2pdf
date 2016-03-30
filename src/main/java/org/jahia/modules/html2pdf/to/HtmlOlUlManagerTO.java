package org.jahia.modules.html2pdf.to;

import java.util.HashMap;
import java.util.Map;

/**
 * HtmlOlUlManagerTO Class
 */
public class HtmlOlUlManagerTO {

	/* the ol counter */
	private Float olCounter;

	/* the ul counter */
	private Float ulCounter;

	/* the list map */
	private Map<String, Map<Float, Float>> listMap;

	/* the ol constant */
	private final String OL = "ol";

	/* the ul constant */
	private final String UL = "ul";


	/* the HtmlOlUlManagerTO constructor */
	public HtmlOlUlManagerTO(){
		olCounter = 0F;
		ulCounter = 0F;
		listMap   = new HashMap<>();
		listMap.put(OL, new HashMap<Float,Float>());
		listMap.put(UL, new HashMap<Float,Float>());
	}

	/**
	 * <pre>registerOl</pre>
	 * register a new ol tag.
	 *
	 */
	public void registerOl(){
		olCounter++;
	}

	/**
	 * <pre>registerUl</pre>
	 * register a new ul tag.
	 */
	public void registerUl(){
		ulCounter++;
	}

	/**
	 * <pre>unregisterOl</pre>
	 * unregister a ol tag.
	 */
	public void unregisterOl(){
		if(listMap.get(OL).containsKey(olCounter))
			listMap.get(OL).remove(olCounter);
		olCounter--;
	}

	/**
	 * <pre>unregisterUl</pre>
	 * unregister a ul tag.
	 */
	public void unregisterUl(){
		if(listMap.get(UL).containsKey(ulCounter))
			listMap.get(UL).remove(ulCounter);
		ulCounter--;
	}

	/**
	 *<pre>registerOlLi</pre>
	 * register a new li tag with
	 * ol parent tag.
	 */
	public void registerOlLi(){
		if(listMap.get(OL).containsKey(olCounter))
			listMap.get(OL).put(olCounter, listMap.get(OL).get(olCounter) + 1);
		else
			listMap.get(OL).put(olCounter, 1F);
	}

	/**
	 *<pre>registerUlLi</pre>
	 * register a new li tag with
	 * ul parent tag.
	 */
	public void registerUlLi(){
		if(listMap.get(UL).containsKey(ulCounter))
			listMap.get(UL).put(ulCounter, listMap.get(UL).get(ulCounter) + 1);
		else
			listMap.get(UL).put(ulCounter, 1F);
	}

	/**
	 * <pre>getOlLiIndex</pre>
	 * return the li index under
	 * the ol tag.
	 *
	 * @return @Float
     */
	public Float getOlLiIndex(){
		if(listMap.get(OL).containsKey(olCounter))
			return listMap.get(OL).get(olCounter);
		
		return 0F;
	}

	/**
	 * <pre>getUlLiIndex</pre>
	 * return the li index under
	 * the ul tag.
	 *
	 * @return @Float
	 */
	public Float getUlLiIndex(){
		if(listMap.get(UL).containsKey(ulCounter))
			return listMap.get(UL).get(ulCounter);
		
		return 0F;
	}

	/**
	 * <pre>getOlIndex</pre>
	 * Gets the ol index.
	 *
	 * @return @Float
     */
	public Float getOlIndex(){
		return olCounter;
	}

	/**
	 * <pre>getUlIndex</pre>
	 * Gets the ul index.
	 *
	 * @return @Float
	 */
	public Float getUlIndex(){
		return ulCounter;
	}

}
