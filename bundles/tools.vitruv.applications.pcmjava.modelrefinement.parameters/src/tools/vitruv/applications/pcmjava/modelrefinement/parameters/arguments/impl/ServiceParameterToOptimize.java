package tools.vitruv.applications.pcmjava.modelrefinement.parameters.arguments.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import tools.vitruv.applications.pcmjava.modelrefinement.parameters.loop.impl.LoopModel;
import weka.core.Instance;

public class ServiceParameterToOptimize {
	private String name;
	private ArgumentModel argModel;
	private LoopModel loopModel;
	private Map<String, List<Object>> attributes;

	public ServiceParameterToOptimize(String name, ArgumentModel model) {
		this.name = name;
		this.argModel = model;
		this.attributes = this.argModel.getDataSet().getAttrForValues();
	}

	public ServiceParameterToOptimize(String name, LoopModel model) {
		this.name = name;
		this.loopModel = model;
		this.attributes = this.argModel.getDataSet().getAttrForValues();
	}

	/**
	 * Get map of the numeric attributes of the parameter data set with their values
	 * 
	 * @return Map<attrName, List<attrValues>
	 */
	public Map<String, List<Object>> getNumericAttr() {
		Map<String, List<Object>> result = new HashMap<String, List<Object>>();
		for (Entry<String, List<Object>> attr : attributes.entrySet()) {
			if (attr.getValue().get(0) instanceof Double) {
				result.put(attr.getKey(), attr.getValue());
			}
		}
		return result;
	}

	/**
	 * Get map of the nominal attributes of the parameter data set with their values
	 * 
	 * @return Map<attrName, List<attrValues>
	 */
	public Map<String, List<Object>> getNominalAttr() {
		Map<String, List<Object>> result = new HashMap<String, List<Object>>();

		for (Entry<String, List<Object>> attr : attributes.entrySet()) {
			if (attr.getValue().get(0) instanceof String) {
				result.put(attr.getKey(), attr.getValue());
			}
		}
		return result;
	}

	public String getName() {
		return name;
	}

	public ArgumentModel getModel() {
		return argModel;
	}

	public void printAttr() {
		attributes.forEach((s, l) -> {
			System.out.println(s + ": " + l.toString());
		});
	}

}
