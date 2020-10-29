package utility.math;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Random;
import java.util.function.BiPredicate;
import java.util.function.Consumer;
import java.util.function.Function;

import utility.Utils;

//import utility.Function;

public class Matrix {
	float scaler;
	float[][] matrix;
	float determinant;
	public static void main(String[] args) //testing purposes
	{
		Matrix mat = new Matrix(4, 4, 1);
		float[][] arr = {
				{3, 5, 4, 2},
				{4, 7, 2, 3},
				{4, 3, 9, 6},
				{7, 5, 5, 3}
		};
		mat.set(arr);
		mat.calculateDeterminant();
		System.out.println("determinant: " + mat.determinant);
	}
	public Matrix(int rows, int cols, float scaler)
	{
		this.scaler = scaler;
		matrix = new float[rows][cols];
	}
	public void randomize(float min, float max)
	{
		for(int r = 0; r < matrix.length; r++)
		{
			for(int c = 0; c < matrix[r].length; c++)
			{
				matrix[r][c] = (float) Utils.rand(min, max);
			}
		}
	}
	public Matrix copy()
	{
		Matrix m = new Matrix(matrix.length, matrix[0].length, scaler);
		for(int i = 0; i < matrix.length; i++)
			for(int j = 0; j < matrix[i].length; j++)
				m.matrix[i][j] = matrix[i][j];
		return m;
	}
	public int getRows()
	{
		return matrix.length;
	}
	public int getCols()
	{
		return matrix[0].length;
	}
	public void mutate(float rate, float strength)
	{
		Random random = new Random();
		for(int i = 0; i < matrix.length; i++)
			for(int j = 0; j < matrix[i].length; j++)
				if(Math.random() < rate)
					matrix[i][j] += random.nextGaussian()*strength;
	}
	
	public static Matrix fromArray(float[] arr)
	{
		Matrix matrix = new Matrix(arr.length, 1, 1);
		for(int i = 0; i < arr.length; i++)
			matrix.matrix[i][0] = arr[i];
		return matrix;
	}
	
	public float[][] toArray()
	{
		float[][] arr = new float[matrix.length][matrix[0].length];
		for(int r = 0; r < matrix.length; r++)
		{
			for(int c = 0; c < matrix[r].length; c++)
			{
				arr[r][c] = matrix[r][c];
			}
		}
		return arr;
	}
	/**
	 * @deprecated - needs to be fixed, doesn't work
	 */
	public void calculateDeterminant()
	{
		if(matrix.length != matrix[0].length)
		{
			determinant = -1;
			return;
		}
		if(matrix.length == 2)
		{
			determinant = matrix[0][0]*matrix[1][1] + matrix[0][1]*matrix[1][0];
			return;
		}
		Matrix temp = extend();
		for(int i = 0; i < matrix.length; i++)
		{
			
		}
		determinant = determinant(matrix);
	}
	private float determinant(float[][] mat)
	{
		if(mat.length == mat[0].length && mat.length == 2)
		{
			return mat[0][0]*mat[1][1] - mat[0][1]*mat[1][0];
		}
		else
		{
			float det = 0;
			int mult = 1;
			for(int i = 0; i < mat[0].length; i++)
			{
				float[][] mat1 = new float[mat.length-1][mat[0].length-1];
				int c1 = 0;
				
				for(int c = 0; c < mat[0].length; c++)
				{
					if(c == i) //skip the row of the current column
					{
						c++;
						continue;
					}
					for(int r = 1; r < mat.length; r++)
					{
						mat1[r-1][c1] = mat[r][c];
					}
					c1++;
				}
				det += mult*(mat[0][i]*determinant(mat1));
				mult *= -1;
			}
			return det;
		}
	}
	private Matrix extend()
	{
		float[][] mat = new float[matrix.length][matrix[0].length*2 - 1];
		for(int r = 0; r < matrix.length; r++)
		{
			for(int c = 0; c < matrix[r].length; c++)
			{
				mat[r][c] = matrix[r][c];
				if(c != matrix[r].length-1)
					mat[r][c+matrix[0].length] = matrix[r][c]; 
			}
		}
		Matrix newMatrix = new Matrix(mat.length, mat[0].length, scaler);
		newMatrix.set(mat);
		return newMatrix;
	}
	
	public void print()
	{
		for(int i = 0; i < matrix.length; i++)
		{
			System.out.print("[");
			for(int j = 0; j < matrix[i].length; j++)
			{
				if(j == matrix[i].length - 1)
					System.out.print(matrix[i][j]);
				else
					System.out.print(matrix[i][j] + ", ");
			}
			System.out.print("]");
			System.out.println();
		}
	}
	
	public void set(float[][] matrix)
	{
		for(int r = 0; r < matrix.length; r++)
		{
			for(int c = 0; c < matrix[r].length; c++)
			{
				this.matrix[r][c] = matrix[r][c];
			}
		}
		//calculateDeterminant();
	}
	public void set(int r, int c, float n)
	{
		matrix[r][c] = n;
		//calculateDeterminant();
	}
	public float get(int r, int c)
	{
		return scaler*matrix[r][c];
	}
	
	public Matrix add(Matrix other)
	{
		Matrix newMatrix = new Matrix(matrix.length, matrix[0].length, 1);
		for(int i = 0; i < matrix.length; i++)
		{
			for(int j = 0; j < matrix[i].length; j++)
			{
				newMatrix.set(i, j, matrix[i][j] + other.matrix[i][j]);
			}
		}
		return newMatrix;
	}
	
	public void addLocal(Matrix other)
	{
		for(int i = 0; i < matrix.length; i++)
		{
			for(int j = 0; j < matrix[i].length; j++)
			{
				matrix[i][j] += other.matrix[i][j];
			}
		}
	}
	public void addLocal(float n)
	{
		for(int i = 0; i < matrix.length; i++)
		{
			for(int j = 0; j < matrix[i].length; j++)
			{
				matrix[i][j] += n;
			}
		}
	}
	public void hadamardLocal(Matrix other)
	{
		for(int i = 0; i < matrix.length; i++)
		{
			for(int j = 0; j < matrix[i].length; j++)
			{
				matrix[i][j] *= other.matrix[i][j];
			}
		}
	}
	public void multLocal(float n)
	{
		for(int i = 0; i < matrix.length; i++)
		{
			for(int j = 0; j < matrix[i].length; j++)
			{
				matrix[i][j] *= n;
			}
		}
	}
	
	public void map(Function<Float, Float> f)
	{
		for(int i = 0; i < matrix.length; i++)
		{
			for(int j = 0; j < matrix[i].length; j++)
			{
				matrix[i][j] = f.apply(matrix[i][j]);
			}
		}
	}
	public static Matrix map(Matrix a, Function<Float, Float> f)
	{
		Matrix result = new Matrix(a.matrix.length, a.matrix[0].length, 1);
		for(int i = 0; i < a.matrix.length; i++)
		{
			for(int j = 0; j < a.matrix[i].length; j++)
			{
				result.matrix[i][j] = f.apply(a.matrix[i][j]);
			}
		}
		return result;
	}
	
	public Matrix subtract(Matrix other)
	{
		Matrix newMatrix = new Matrix(matrix.length, matrix[0].length, 1);
		for(int i = 0; i < matrix.length; i++)
		{
			for(int j = 0; j < matrix[i].length; j++)
			{
				newMatrix.set(i, j, matrix[i][j] - other.matrix[i][j]);
			}
		}
		return newMatrix;
	}
	/**
	 * 
	 * @param other - the other matrix being multiplied
	 * @return - a new matrix that is the product of the other matrices
	 */
	public Matrix mult(Matrix other)
	{
		int rows = matrix.length;
		int cols = other.matrix[0].length;
//		if(other.matrix.length > rows)
//			rows = other.matrix.length;
//		if(other.matrix[0].length > cols)
//			cols = other.matrix[0].length;
		
		Matrix newMatrix = new Matrix(rows, cols, 1);
		float[][] matrix = new float[rows][cols];
		
		for(int r = 0; r < newMatrix.matrix.length; r++)//current row of new matrix
		{
			for(int c = 0; c < newMatrix.matrix[r].length; c++)//current column of new matrix
			{
				float value = 0;
				for(int i = 0; i < this.matrix[r].length; i++)//every number in the row
				{
					value += (get(r, i)*other.get(i, c));
				}
				newMatrix.set(r, c, value);
				//matrix[r][c] = value;
			}
		}
		return newMatrix;
	}
	
	public void multLocal(Matrix other)
	{
		int rows = matrix.length;
		int cols = matrix[0].length;
		if(other.matrix.length > rows)
			rows = other.matrix.length;
		if(other.matrix[0].length > cols)
			cols = other.matrix[0].length;
		
		//Matrix newMatrix = new Matrix(rows, cols, 1);
		float[][] matrix = new float[rows][cols];
		
		for(int r = 0; r < matrix.length; r++)//current row of new matrix
		{
			for(int c = 0; c < matrix[r].length; c++)//current column of new matrix
			{
				float value = 0;
				for(int i = 0; i < matrix[r].length; i++)//every number in the row
				{
					value += (get(r, i)*other.get(i, c));
				}
				//newMatrix.set(r, c, value);
				matrix[r][c] = value;
			}
		}
		this.matrix = matrix;
	}
	public void setMult(Matrix a, Matrix b)
	{
		for(int r = 0; r < matrix.length; r++)//current row of new matrix
		{
			for(int c = 0; c < matrix[r].length; c++)//current column of new matrix
			{
				float value = 0;
				for(int i = 0; i < matrix[r].length; i++)//every number in the row
				{
					value += (a.get(r, i)*b.get(i, c));
				}
				//newMatrix.set(r, c, value);
				matrix[r][c] = value;
			}
		}
	}
	
	public static Matrix mult(Matrix a, Matrix b)
	{
		return a.mult(b);
	}
	
	public static Matrix transpose(Matrix a)
	{
		Matrix newMatrix = new Matrix(a.matrix[0].length, a.matrix.length, a.scaler);
		
		for(int i = 0; i < a.matrix.length; i++)
			for(int j = 0; j < a.matrix[i].length; j++)
				newMatrix.matrix[j][i] = a.matrix[i][j];
		return newMatrix;
	}

}
