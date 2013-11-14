package de.uni_potsdam.hpi.metanome.frontend.server;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

import de.uni_potsdam.hpi.metanome.algorithm_integration.Algorithm;
import de.uni_potsdam.hpi.metanome.algorithm_integration.configuration.ConfigurationSpecification;
import de.uni_potsdam.hpi.metanome.algorithm_integration.configuration.ConfigurationSpecificationBoolean;
import de.uni_potsdam.hpi.metanome.algorithm_integration.configuration.ConfigurationSpecificationCsvFile;
import de.uni_potsdam.hpi.metanome.algorithm_integration.configuration.ConfigurationSpecificationSQLIterator;
import de.uni_potsdam.hpi.metanome.algorithm_integration.configuration.ConfigurationSpecificationString;
import de.uni_potsdam.hpi.metanome.algorithm_loading.AlgorithmJarLoader;
import de.uni_potsdam.hpi.metanome.frontend.client.parameter.InputParameter;
import de.uni_potsdam.hpi.metanome.frontend.client.parameter.InputParameterBoolean;
import de.uni_potsdam.hpi.metanome.frontend.client.parameter.InputParameterCsvFile;
import de.uni_potsdam.hpi.metanome.frontend.client.parameter.InputParameterSQLIterator;
import de.uni_potsdam.hpi.metanome.frontend.client.parameter.InputParameterString;
import de.uni_potsdam.hpi.metanome.frontend.client.services.ParameterService;

/**
 * Service Implementation that provides functionality for retrieving and setting parameters on 
 * algorithms.
 */
public class ParameterServiceImpl extends RemoteServiceServlet implements ParameterService {

	private static final long serialVersionUID = 7343803695093136183L;
		
	/**
	 * 
	 * @param algorithmFileName	name of the algorithm for which the configuration parameters shall be 
	 * 							retrieved
	 * @return	a list of <link>InputParameter</link>s necessary for calling the given algorithm
	 */
	@Override
	public List<InputParameter> retrieveParameters(String algorithmFileName){
		Algorithm algorithm = null;
		AlgorithmJarLoader jarLoader = new AlgorithmJarLoader();
		try {
			algorithm = jarLoader.loadAlgorithm(algorithmFileName);
		} catch (Exception e) {
			System.out.println("FAILED to LOAD algorithm");
			// TODO error handling
		}
		
		List<ConfigurationSpecification> configList = algorithm.getConfigurationRequirements();
		ArrayList<InputParameter> paramList = new ArrayList<InputParameter>();
		
		for (ConfigurationSpecification config : configList){
			paramList.add(getInputParameterInstance(config));
		}
		
		return paramList;
	}
	
	/**
	 * maps the <link>ConfigurationSpecification</link> subclass to the corresponding Type enum
	 * element
	 * 
	 * TODO find a more appropriate style to do this
	 * 
	 * @param config	the ConfigurationSpecification object to be mapped
	 * @return the Type enum 
	 */
	private InputParameter getInputParameterInstance(ConfigurationSpecification config) {
		if (config instanceof ConfigurationSpecificationString) {
			return new InputParameterString(config.getIdentifier());
		} else if (config instanceof ConfigurationSpecificationBoolean) {
			return new InputParameterBoolean(config.getIdentifier());
		} else if (config instanceof ConfigurationSpecificationCsvFile) {
			return new InputParameterCsvFile(config.getIdentifier());
		} else if (config instanceof ConfigurationSpecificationSQLIterator) {
			return new InputParameterSQLIterator(config.getIdentifier());
		} else {
			return null;
		}
	}
	
}
