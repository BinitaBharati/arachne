package com.github.binitabharati.arachne.ui;

import java.util.Collection;
import java.util.Hashtable;

import javax.swing.JFrame;

import com.mxgraph.model.mxGraphModel;
import com.mxgraph.swing.mxGraphComponent;
import com.mxgraph.util.mxConstants;
import com.mxgraph.util.mxUtils;
import com.mxgraph.view.mxGraph;
import com.mxgraph.view.mxStylesheet;

/**
 * 
 * @author binita.bharati@gmail.com
 * Running this class will generate the network diagram on a Swing JFrame.
 *
 */

public class NetworkDiagram extends JFrame
{

	/**
	 * 
	 */
	private static final long serialVersionUID = -2707712944901661771L;

	public NetworkDiagram()
	{
		super("Network Diagram");

		mxGraph graph = new mxGraph();
		Object parent = graph.getDefaultParent();

		graph.getModel().beginUpdate();
		try
		{
			mxStylesheet stylesheet = graph.getStylesheet();
			
			Hashtable<String, Object> style = new Hashtable<String, Object>();
			style.put(mxConstants.STYLE_SHAPE, mxConstants.SHAPE_HEXAGON);
			style.put(mxConstants.STYLE_OPACITY, 50);
			style.put(mxConstants.STYLE_FONTCOLOR, "#774400");
			stylesheet.putCellStyle("ROUNDED", style);
			
			String hubStyle = "fillColor=green";
			
			
			Object v1 = graph.insertVertex(parent, null, "Network1-HUB", 120, 120, 80, 30, hubStyle);
			Object v2 = graph.insertVertex(parent, null, "192.168.10.12", 60, 20,
					80, 30);
			Object v3 = graph.insertVertex(parent, null, "192.168.10.13", 20, 160,
					80, 30);
			Object v4 = graph.insertVertex(parent, null, "ROUTER-1", 290, 100,
					80, 30);
			Object v5 = graph.insertVertex(parent, null, "Network2-HUB", 460, 100,
					80, 30, hubStyle);
			Object v6 = graph.insertVertex(parent, null, "192.168.20.13", 460, 20,
					80, 30);
			Object v7 = graph.insertVertex(parent, null, "192.168.20.14", 590, 20,
					80, 30);
			Object v8 = graph.insertVertex(parent, null, "ROUTER-2", 620, 100,
					80, 30);
			Object v9 = graph.insertVertex(parent, null, "Network3-HUB", 620, 180,
					80, 30, hubStyle);
			Object v10 = graph.insertVertex(parent, null, "ROUTER-3", 450, 180,
					80, 30);
			Object v11 = graph.insertVertex(parent, null, "192.168.30.13", 620, 260,
					80, 30);
			Object v12 = graph.insertVertex(parent, null, "Network4-HUB", 280, 180,
					80, 30, hubStyle);
			Object v13 = graph.insertVertex(parent, null, "192.168.40.12", 280, 260,
					80, 30);
			
			graph.insertEdge(parent, null, "", v1, v2, mxConstants.NONE);
			graph.insertEdge(parent, null, "", v1, v3, mxConstants.NONE);
			graph.insertEdge(parent, null, "192.168.10.11", v1, v4, mxConstants.NONE);
			graph.insertEdge(parent, null, "192.168.20.11", v4, v5, mxConstants.NONE);
			graph.insertEdge(parent, null, "", v5, v6, mxConstants.NONE);
			graph.insertEdge(parent, null, "", v5, v7, mxConstants.NONE);
			graph.insertEdge(parent, null, "192.168.20.12", v5, v8, mxConstants.NONE);
			graph.insertEdge(parent, null, "192.168.30.11", v8, v9, mxConstants.NONE);
			graph.insertEdge(parent, null, "192.168.30.12", v9, v10, mxConstants.NONE);
			graph.insertEdge(parent, null, "", v9, v11, mxConstants.NONE);
			graph.insertEdge(parent, null, "192.168.40.13", v10, v12, mxConstants.NONE);
			graph.insertEdge(parent, null, "", v12, v13, mxConstants.NONE);
			graph.insertEdge(parent, null, "192.168.40.11", v4, v12, mxConstants.NONE);
			
			//graph.createEdge(parent, null, "Test", v1, v2, mxConstants.ELBOW_HORIZONTAL);
		}
		finally
		{
			mxGraphModel graphModel  = (mxGraphModel)graph.getModel(); 
			Collection<Object> cells =  graphModel.getCells().values(); 
			mxUtils.setCellStyles(graphModel, 
			    cells.toArray(), mxConstants.STYLE_ENDARROW, mxConstants.NONE);
			graphModel.endUpdate();
		}

		mxGraphComponent graphComponent = new mxGraphComponent(graph);
		getContentPane().add(graphComponent);
	}

	public static void main(String[] args)
	{
		NetworkDiagram frame = new NetworkDiagram();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setExtendedState(java.awt.Frame.MAXIMIZED_BOTH);
		frame.setSize(400, 320);
		frame.setVisible(true);
	}

}
