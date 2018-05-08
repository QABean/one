package cn.migu.newportal.selection.controller;
/*package org.ms.selection.controller;

import java.util.Collection;
import java.util.Map;
import java.util.TreeMap;

import javax.annotation.Resource;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.google.protobuf.Descriptors;

import cn.migu.wheat.service.ServiceManager;
import cn.migu.wheat.service.ServiceProvider;

@Controller
@RequestMapping(produces = "application/json; charset=UTF-8")
public class StatusController {
	@Resource ServiceManager serviceManager;

	@RequestMapping(value="/", method=RequestMethod.GET)
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	public Collection<String> home(){
		return serviceManager.list();
	}

	@RequestMapping(value="/status", method=RequestMethod.GET)
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	public String status(){
		return "OK";
	}
	
	@RequestMapping(value="/stop", method=RequestMethod.GET)
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	public String stop(){
		serviceManager.getContext().getRegistryFactory().stop();
		return "OK";
	}

	static class MethodDesc {
		Descriptors.Descriptor inputType;
		Descriptors.Descriptor outputType;

		public String getInputType() {
			return inputType.getName();
		}
		public void setInputType(Descriptors.Descriptor inputType) {
			this.inputType = inputType;
		}
		public String getOutputType() {
			return outputType.getName();
		}
		public void setOutputType(Descriptors.Descriptor outputType) {
			this.outputType = outputType;
		}
	}

	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	@RequestMapping(value="/{service}", method=RequestMethod.GET)
	public ResponseEntity<Map<String, MethodDesc>> list(@PathVariable("service") String service) {
		ServiceProvider svc = serviceManager.get(service);
		if(null != svc){
			Map<String, MethodDesc> result = new TreeMap<String, MethodDesc>();
			for(Descriptors.MethodDescriptor method: svc.getMethods()){
				MethodDesc desc = new MethodDesc();
				desc.setInputType(method.getInputType());
				desc.setOutputType(method.getOutputType());
				result.put(method.getName(), desc);
			}
			return new ResponseEntity<Map<String, MethodDesc>>(result, HttpStatus.OK);
		}else{
			return new ResponseEntity<Map<String, MethodDesc>>(HttpStatus.NOT_FOUND);
		}
	}
	
}
*/