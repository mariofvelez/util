package machine_learning;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.Random;

import utility.math.Matrix;

public class NeuralNetwork implements Cloneable {
	public int input_nodes;
	public int[] hidden_nodes;
	public int output_nodes;
	
	Matrix[] weights; //all weights between node layers
	Matrix[] biases; //biases for node layers
	
	Matrix[] hidden_thresh;
	
	float learning_rate = 0.1f;
	
	public NeuralNetwork(int input_nodes, int hidden_nodes, int output_nodes)
	{
		this.input_nodes = input_nodes;
		this.hidden_nodes = new int[1];
		this.hidden_nodes[0] = hidden_nodes;
		this.output_nodes = output_nodes;
		
		weights = new Matrix[2];
		weights[0] = new Matrix(hidden_nodes, input_nodes, 1);
		weights[1] = new Matrix(output_nodes, hidden_nodes, 1);
		
		for(int i = 0; i < weights.length; i++)
			weights[i].randomize(-1, 1);
		
		biases = new Matrix[2];
		biases[0] = new Matrix(hidden_nodes, 1, 1);
		biases[1] = new Matrix(output_nodes, 1, 1);
		biases[0].randomize(-1, 1);
		biases[1].randomize(-1, 1);
		
		hidden_thresh = new Matrix[weights.length-1];
	}
	
	public NeuralNetwork(int... nodes)
	{
		input_nodes = nodes[0];
		if(nodes.length > 2)
			hidden_nodes = new int[nodes.length-2];
		else
			hidden_nodes = new int[0];
		for(int i = 1; i < nodes.length-1; i++)
			hidden_nodes[i-1] = nodes[i];
		output_nodes = nodes[nodes.length-1];
		
		weights = new Matrix[nodes.length-1];
		for(int i = 1; i < nodes.length; i++)
		{
			weights[i-1] = new Matrix(nodes[i], nodes[i-1], 1);
			weights[i-1].randomize(-1, 1);
		}
		
		biases = new Matrix[nodes.length-1];
		for(int i = 1; i < nodes.length; i++)
		{
			biases[i-1] = new Matrix(nodes[i], 1, 1);
			biases[i-1].randomize(-1, 1);
		}
		
		hidden_thresh = new Matrix[weights.length-1];
	}
	
	public Matrix feedforward(float[] inputs_array)
	{
		Matrix inputs = Matrix.fromArray(inputs_array); //input to matrix
		hidden_thresh[0] = Matrix.mult(weights[0], inputs); //hidden nodes
		hidden_thresh[0].addLocal(biases[0]); //adding biases
		hidden_thresh[0].map(NeuralNetwork::sigmoid);
		
		for(int i = 1; i < weights.length-1; i++)
		{
			hidden_thresh[i] = Matrix.mult(weights[i], hidden_thresh[i-1]);
			hidden_thresh[i].addLocal(biases[i]);
			hidden_thresh[i].map(NeuralNetwork::sigmoid);
		}
		
		Matrix outputs = Matrix.mult(weights[weights.length-1], hidden_thresh[hidden_thresh.length-1]);
		outputs.addLocal(biases[biases.length-1]);
		outputs.map(NeuralNetwork::sigmoid);
		return outputs;
	}
	
	public void train(float[] inputs_array, float[] targets_array)
	{
		//feeding forward the neural network
		Matrix inputs = Matrix.fromArray(inputs_array); //input to matrix
		hidden_thresh[0] = Matrix.mult(weights[0], inputs); //hidden nodes
		hidden_thresh[0].addLocal(biases[0]); //adding biases
		hidden_thresh[0].map(NeuralNetwork::sigmoid);
		
		for(int i = 1; i < weights.length-1; i++)
		{
			hidden_thresh[i] = Matrix.mult(weights[i], hidden_thresh[i-1]);
			hidden_thresh[i].addLocal(biases[i]);
			hidden_thresh[i].map(NeuralNetwork::sigmoid);
		}
		
		Matrix outputs = Matrix.mult(weights[weights.length-1], hidden_thresh[hidden_thresh.length-1]);
		outputs.addLocal(biases[biases.length-1]);
		outputs.map(NeuralNetwork::sigmoid);
		
		Matrix targets = Matrix.fromArray(targets_array);
		Matrix errors = targets.subtract(outputs);
		
		//calculate gradient
		Matrix gradients = Matrix.map(outputs, NeuralNetwork::dsigmoid);
		gradients.hadamardLocal(errors);
		gradients.multLocal(learning_rate);
		
		//calculate delta weights for output
		Matrix hidden_T = Matrix.transpose(hidden_thresh[hidden_thresh.length-1]);
		Matrix weight_ho_deltas = Matrix.mult(gradients, hidden_T);
		
		//adding change in weights
		weights[weights.length-1].addLocal(weight_ho_deltas);
		//adding change in bias
		biases[biases.length-1].addLocal(gradients);
		
		//iterating backwards through weights
		for(int i = weights.length-2; i > 0; i--)
		{
			//i == the current weights index
			//calculating hidden layer errors
			Matrix weight_T = Matrix.transpose(weights[i+1]);
			errors = Matrix.mult(weight_T, errors);
			
			//calculate hidden gradient
			Matrix hidden_gradients = Matrix.map(hidden_thresh[i], NeuralNetwork::dsigmoid);
			hidden_gradients.hadamardLocal(errors);
			hidden_gradients.multLocal(learning_rate);
			
			//calculate delta weights
			Matrix p_weight_T = Matrix.transpose(hidden_thresh[i-1]);
			Matrix weight_deltas = Matrix.mult(hidden_gradients, p_weight_T);
			
			weights[i].addLocal(weight_deltas);
			biases[i].addLocal(hidden_gradients);
		}
		
		//calculate hidden layer errors
		Matrix w_ho_t = Matrix.transpose(weights[1]);
		errors = Matrix.mult(w_ho_t, errors);
		
		//calculate hidden gradient
		Matrix hidden_gradient = Matrix.map(hidden_thresh[0], NeuralNetwork::dsigmoid);
		hidden_gradient.hadamardLocal(errors);
		hidden_gradient.multLocal(learning_rate);
		
		//calculate hidden -> input deltas
		Matrix input_T = Matrix.transpose(inputs);
		Matrix weight_ih_deltas = Matrix.mult(hidden_gradient, input_T);
		
		//adding change in weights
		weights[0].addLocal(weight_ih_deltas);
		//adding change in bias
		biases[0].addLocal(hidden_gradient);
		
		//outputs.print();
		//targets.print();
		//errors.print();
	}
	
	public void mutate(float rate, float strength)
	{
		for(int i = 0; i < weights.length; i++)
			weights[i].mutate(rate, strength);
		for(int i = 0; i < biases.length; i++)
			biases[i].mutate(rate, strength);
	}
	public NeuralNetwork pMutate(float rate, float strength)
	{
		int[] nodes = new int[hidden_nodes.length+2];
		nodes[0] = input_nodes;
		for(int i = 1; i < nodes.length-1; i++)
			nodes[i] = hidden_nodes[i-1];
		nodes[nodes.length-1] = output_nodes;
		NeuralNetwork nn = new NeuralNetwork(nodes);
		nn.mutate(rate, strength);
		
		return nn;
	}
	public NeuralNetwork clone()
	{
		int[] nodes = new int[hidden_nodes.length+2];
		nodes[0] = input_nodes;
		for(int i = 1; i < nodes.length-1; i++)
			nodes[i] = hidden_nodes[i-1];
		nodes[nodes.length-1] = output_nodes;
		NeuralNetwork nn = new NeuralNetwork(nodes);
		return nn;
	}
	
	public static float sigmoid(float x)
	{
		return 1 / (1+(float)Math.exp(-x));
	}
	public static float dsigmoid(float y)
	{
		//return sigmoid(y) * (1-sigmoid(y));
		return y * (1-y);
	}
	
	public void draw(Graphics g, int x, int y)
	{
		int dx = 100;
		int dy = 20;
		Graphics2D g2 = (Graphics2D)g;
		int[][] p_xy = new int[input_nodes][2]; //previous layer's points
		boolean contract = input_nodes > 30;
		for(int i = 0, j = 0; i < input_nodes; i++, j++)
		{
			if(contract && i == 0)
				i = input_nodes/2 - 15;
			if(contract && j == 30)
				break;
			p_xy[i][0] = x;
			p_xy[i][1] = y + i*dy - input_nodes*dy/2;
			
			if(contract && i < input_nodes/2)
				p_xy[i][1] -= dy;
			else if(contract)
				p_xy[i][1] += dy;
		}
		for(int i = 0; i < hidden_nodes.length; i++) //for all hidden layers
		{
			int[][] xy = new int[hidden_nodes[i]][2];
			for(int j = 0; j < hidden_nodes[i]; j++) //for all nodes in each layer
			{
				xy[j][0] = x + (i+1)*dx;
				xy[j][1] = y + j*dy - hidden_nodes[i]*dy/2;
				for(int k = 0; k < p_xy.length; k++) //for all previous nodes in the previous layer
				{
					if(p_xy[k][0] != 0 && p_xy[k][1] != 0)
					{
						g2.setColor(getColor(weights[i].get(j, k)));
						g2.drawLine(xy[j][0], xy[j][1], p_xy[k][0], p_xy[k][1]);
					}
				}
			}
			p_xy = xy;
		}
		int[][] o_xy = new int[output_nodes][2];
		for(int i = 0; i < output_nodes; i++)
		{
			o_xy[i][0] = x + (hidden_nodes.length+1)*dx;
			o_xy[i][1] = y + i*dy - output_nodes*dy/2;
			for(int j = 0; j < p_xy.length; j++) //for all previous nodes in the previous layer
			{
				g2.setColor(getColor(weights[weights.length-1].get(i, j)));
				g2.drawLine(o_xy[i][0], o_xy[i][1], p_xy[j][0], p_xy[j][1]);
			}
		}
		for(int i = 0, j = 0; i < input_nodes; i++, j++)
		{
			if(contract && i == 0)
				i = input_nodes/2 - 15;
			if(contract && j == 30)
				break;
			int[] pos = {x-5, y + i*dy - input_nodes*dy/2 - 5};
			if(contract && i < input_nodes/2)
				pos[1] -= dy;
			else if(contract)
				pos[1] += dy;
			g2.setColor(Color.BLACK);
			g2.drawOval(pos[0], pos[1], 10, 10);
			g2.setColor(Color.CYAN);
			g2.fillOval(pos[0], pos[1], 10, 10);
		}
		for(int h = 0; h < hidden_nodes.length; h++)
		{
			for(int i = 0; i < hidden_nodes[h]; i++)
			{
				g2.setColor(Color.BLACK);
				g2.drawOval(x + (h+1)*dx - 5, y + i*dy - hidden_nodes[h]*dy/2 - 5, 10, 10);
				g2.setColor(getColor(biases[h].get(i, 0)));
				g2.fillOval(x + (h+1)*dx - 5, y + i*dy - hidden_nodes[h]*dy/2 - 5, 10, 10);
			}
		}
		for(int o = 0; o < output_nodes; o++)
		{
			g2.setColor(Color.BLACK);
			g2.drawOval(x + (hidden_nodes.length+1)*dx - 5, y + o*dy - output_nodes*dy/2 - 5, 10, 10);
			g2.setColor(getColor(biases[biases.length-1].get(o, 0)));
			g2.fillOval(x + (hidden_nodes.length+1)*dx - 5, y + o*dy - output_nodes*dy/2 - 5, 10, 10);
		}
		if(contract)
		{
			g2.setColor(Color.BLACK);
			for(int i = 0; i < 3; i++)
				g2.fillOval(x-2, y-18 + i*8, 4, 4);
		}
	}
	private Color getColor(float weight)
	{
		//float a =  weight / 10f;
		
		try {
			int a = (int) (weight*127 + 127);
			return new Color(255 - a, 255, a);
		} catch(Exception e) {
			return new Color(255, 255, 127);
		}
	}
	public void printWeightsAndBiases()
	{
		for(int i = 0; i < weights.length; i++)
		{
			System.out.println("weight " + i + ":");
			weights[i].print();
		}
		for(int i = 0; i < biases.length; i++)
		{
			System.out.println("bias " + i + ":");
			biases[i].print();
		}
	}

}
