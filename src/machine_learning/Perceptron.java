package machine_learning;

import java.awt.Color;
import java.awt.Graphics2D;

import utility.Utils;

public class Perceptron {
	public float[] weights;
	float learningRate = 0.000008f;
	
	public Perceptron(int n)
	{
		weights = new float[n];
		for(int i = 0; i < weights.length; i++)
			weights[i] = (float) Utils.rand(-1, 1);
	}
	
	public int guess(float[] inputs)
	{
		float sum = 0;
		for(int i = 0; i < inputs.length; i++)
			sum += inputs[i]*weights[i];
		return Utils.sign(sum);
	}
	
	public void train(float[] inputs, int target)
	{
		 int guess = guess(inputs);
		 int error = target - guess;
		 for(int i = 0; i < weights.length; i++)
			 weights[i] += error * inputs[i] * learningRate;
	}
	
	public float guessY(float x)
	{
		float w0 = weights[0];
		float w1 = weights[1];
		float w2 = weights[2];
		
		return -(w2/w1) - (w0/w1) * x;
	}

}
