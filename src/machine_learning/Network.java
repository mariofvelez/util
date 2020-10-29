package machine_learning;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Arrays;

import utility.Utils;
import utility.math.Matrix;

public class Network implements Serializable {
	private double[][] output;
	public double[][][] weights;
	public double[][] bias;
	
	private double[][] error_signal;
	private double[][] output_derivative;
	
	public final int[] NETWORK_LAYER_SIZES;
	public final int INPUT_SIZE;
	public final int OUTPUT_SIZE;
	public final int NETWORK_SIZE;
	
	public Network(int... NETWORK_LAYER_SIZES)
	{
		this.NETWORK_LAYER_SIZES = NETWORK_LAYER_SIZES;
		this.INPUT_SIZE = NETWORK_LAYER_SIZES[0];
		this.OUTPUT_SIZE = NETWORK_LAYER_SIZES[NETWORK_LAYER_SIZES.length-1];
		this.NETWORK_SIZE = NETWORK_LAYER_SIZES.length;
		
		this.output = new double[NETWORK_SIZE][];
		this.weights = new double[NETWORK_SIZE][][];
		this.bias = new double[NETWORK_SIZE][];
		
		this.error_signal = new double[NETWORK_SIZE][];
		this.output_derivative = new double[NETWORK_SIZE][];
		
		for(int i = 0; i < NETWORK_SIZE; i++)
		{
			this.output[i] = new double[NETWORK_LAYER_SIZES[i]];
			this.error_signal[i] = new double[NETWORK_LAYER_SIZES[i]];
			this.output_derivative[i] = new double[NETWORK_LAYER_SIZES[i]];
						
			this.bias[i] = new double[NETWORK_LAYER_SIZES[i]];
			
			if(i > 0)
				weights[i] = new double[NETWORK_LAYER_SIZES[i]][NETWORK_LAYER_SIZES[i-1]];
		}
		
		for(int i = 0; i < output.length; i++)
			for(int j = 0; j < output[i].length; j++)
				output[i][j] = Utils.rand(0.0, 0.9);
		for(int i = 0; i < bias.length; i++)
			for(int j = 0; j < bias[i].length; j++)
				bias[i][j] = Utils.rand(0.0, 0.9);
		for(int i = 1; i < weights.length; i++)
			for(int j = 0; j < weights[i].length; j++)
				for(int k = 0; k < weights[i][j].length; k++)
					weights[i][j][k] = Utils.rand(-0.9,  0.9);
	}
	public double[] calculate(double... input)
	{
		if(input.length != this.INPUT_SIZE) return null;
		this.output[0] = input;
		for(int layer = 1; layer < NETWORK_SIZE; layer++)
		{
			for(int neuron = 0; neuron < NETWORK_LAYER_SIZES[layer]; neuron++)
			{
				double sum = bias[layer][neuron];;
				for(int prevNeuron = 0; prevNeuron < NETWORK_LAYER_SIZES[layer-1]; prevNeuron++)
					sum += output[layer-1][prevNeuron] * weights[layer][neuron][prevNeuron];
				output[layer][neuron] = sigmoid(sum);
				output_derivative[layer][neuron] = output[layer][neuron] * (1 - output[layer][neuron]);
			}
		}
		return output[NETWORK_SIZE-1].clone();
	}
	public double[][] calculate(double[]... input)
	{
		double[][] output = new double[input.length][];
		for(int i = 0; i < input.length; i++)
			output[i] = calculate(input[i]);
		return output;
	}
	public double MSE(double[] input, double[] target)
	{
		if(input.length != INPUT_SIZE || output.length != OUTPUT_SIZE) return -1;
		calculate(input);
		double v = 0;
		for(int i = 0; i < target.length; i++)
			v += (target[i] - output[NETWORK_SIZE-1][i]) * (target[i] - output[NETWORK_SIZE-1][i]);
		return v / (2d*target.length);
	}
	public double MSE(TrainSet set)
	{
		double v = 0;
		for(int i = 0; i < set.size(); i++)
			v += MSE(set.getInput(i), set.getOutput(i));
		return v / set.size();
	}
	public void train(TrainSet set, int loops, int batch_size, double alpha)
	{
		if(set.INPUT_SIZE != INPUT_SIZE || set.OUTPUT_SIZE != OUTPUT_SIZE)
			return;
		for(int i = 0; i < loops; i++)
		{
			TrainSet batch = set.extractBatch(batch_size);
			for(int b = 0; b < batch.size(); b++)
			{
				this.train(batch.getInput(b), batch.getOutput(b), alpha);
			}
			//System.out.println(MSE(batch));
		}
	}
	public void train(double[] input, double[] target, double eta)
	{
		if(input.length != INPUT_SIZE) return;
		calculate(input);
		backpropogateError(target);
		updateWeights(eta);
	}
	public void backpropogateError(double[] target)
	{
		for(int neuron = 0; neuron < NETWORK_LAYER_SIZES[NETWORK_SIZE-1]; neuron++)
			error_signal[NETWORK_SIZE-1][neuron] = (output[NETWORK_SIZE-1][neuron] - target[neuron]) * 
													output_derivative[NETWORK_SIZE-1][neuron];
		for(int layer = NETWORK_SIZE-2; layer > 0; layer--)
		{
			for(int neuron = 0; neuron < NETWORK_LAYER_SIZES[layer]; neuron++)
			{
				double sum = 0;
				for(int nextNeuron = 0; nextNeuron < NETWORK_LAYER_SIZES[layer+1]; nextNeuron++)
					sum += weights[layer+1][nextNeuron][neuron] * error_signal[layer+1][nextNeuron];
				this.error_signal[layer][neuron] = sum * output_derivative[layer][neuron];
			}
		}
	}
	public void updateWeights(double eta)
	{
		for(int layer = 1; layer < NETWORK_SIZE; layer++)
		{
			for(int neuron = 0; neuron < NETWORK_LAYER_SIZES[layer]; neuron++)
			{
				double delta = - eta * error_signal[layer][neuron];
				bias[layer][neuron] += delta;
				for(int prevNeuron = 0; prevNeuron < NETWORK_LAYER_SIZES[layer-1]; prevNeuron++)
					weights[layer][neuron][prevNeuron] += delta * output[layer-1][prevNeuron];
			}
		}
	}
	public Network clone()
	{
		Network net = new Network(NETWORK_LAYER_SIZES);
		for(int i = 1; i < weights.length; i++)
			for(int j = 0; j < weights[i].length; j++)
				for(int k = 0; k < weights[i][j].length; k++)
					net.weights[i][j][k] = weights[i][j][k];
		for(int i = 0; i < bias.length; i++)
			for(int j = 0; j < bias[i].length; j++)
				net.bias[i][j] = bias[i][j];
		return net;
	}
	public String toString()
	{
		String string = "null" + "\n";
		for(int i = 1; i < weights.length; i++)
			for(int j = 0; j < weights[i].length; j++)
				string += Arrays.toString(weights[i][j]) + "\n";
		for(int i = 0; i < bias.length; i++)
			string += Arrays.toString(bias[i]) + "\n";
		return string;
	}
	public static double sigmoid(double x)
	{
		return 1d / (1+Math.exp(-x));
	}
	public static void main(String[] args)
	{
		Network network;
		try {
			network = Network.loadNetwork("src/test.txt");
			System.out.println(Arrays.toString(network.NETWORK_LAYER_SIZES));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public void saveNetwork(String file) throws Exception
	{
		File f = new File(file);
		ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(f));
		out.writeObject(this);
		out.flush();
		out.close();
	}
	public static Network loadNetwork(String file) throws Exception
	{
		File f = new File(file);
		ObjectInputStream in = new ObjectInputStream(new FileInputStream(f));
		Network net = (Network) in.readObject();
		in.close();
		return net;
	}
}
