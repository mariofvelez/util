package utility.math;

/**
 * Provides fast computations for multidimensional linear transformations
 * @author Mario Velez
 *
 */
public class Transform {
	
	/**
	 * n*n matrix that holds transformation data
	 */
	protected float[] data;
	/**
	 * the origin of the transform
	 */
	protected int dim;
	protected int len;
	
	private final float[] temp;
	
	/**
	 * 
	 * @param dim - number of dimensions
	 * @param zeros - whether to initialize with all zeros or in identity form
	 */
	public Transform(int dim, boolean identity)
	{
		data = new float[dim*dim];
		len = dim*dim;
		this.dim = dim;
		if(identity)
			for(int i = 0; i < dim; i++)
				data[i*dim + i] = 1;
		temp = new float[dim];
	}
	/**
	 * normalizes all the axes to unit vectors of length 1
	 */
	public void normalize()
	{
		for(int c = 0; c < dim; c++)
		{
			float len = 0;
			for(int r = 0; r < dim; r++)
				len += data[r*dim + c]*data[r*dim + c];
			len = (float) Math.sqrt(len);
			for(int r = 0; r < dim; r++)
				data[r*dim + c] /= len;
		}
	}
	/**
	 * projects the coordinates of the given vector to coordinates in real space. Essentially multiplies the matrix and 
	 * vector together.
	 * @param vec - the vector to project
	 */
	public void project(float[] vec)
	{
		for(int i = 0; i < dim; i++)
		{
			float n = 0;
			for(int j = 0; j < dim; j++)
				n += vec[j]*data[i*dim + j];
			temp[i] = n;
		}
		for(int i = 0; i < dim; i++)
			vec[i] = temp[i];
	}
	public void set(int r, int c, float value)
	{
		data[r*dim + c] = value;
	}
	/**
	 * sets the values in this matrix to match the values in the parameter.
	 * @param data - the values to set this matrix to
	 */
	public void setData(float[] data)
	{
		for(int i = 0; i < len; i++)
			this.data[i] = data[i];
	}
	public float get(int r, int c)
	{
		return data[r*dim + c];
	}
	/**
	 * multiplies each value in this matrix by this number, resulting in a scaled transformation
	 * @param n - the number to multiply by
	 */
	public void mult(float n)
	{
		for(int i = 0; i < len; i++)
			data[i] *= n;
	}
	/**
	 * Alters this transform to the product of this and t
	 * @param t - the transform to apply
	 */
	public void mult(Transform t)
	{
		for(int r = 0; r < dim; r++)
		{
			for(int c = 0; c < dim; c++)
			{
				float value = 0;
				for(int i = 0; i < dim; i++)
					value += data[r*dim + i]*t.data[i*dim + c];
				data[r*dim + c] = value;
			}
		}
	}
	/**
	 * dots the vector with the column of this matrix
	 * @param vec - the vector to dot
	 * @param col - the column to dot
	 * @returns - the dot product of the two vectors
	 */
	public float dot(float[] vec, int col)
	{
		float dot = 0;
		for(int i = 0; i < dim; i++)
			dot += vec[i]*data[i*dim + col];
		return dot;
	}
	/**
	 * crosses the two vectors, only applicable for 3 dimensional transforms. First vector is the column, and the second one is the vector
	 * @param vec - the vector to cross
	 * @param col - the column to cross
	 * @returns the cross product of the two vectors
	 */
	public float[] cross(float[] vec, int col)
	{
		float[] cross = new float[3];
		cross[0] = data[dim + col]*vec[2] - data[dim*2 + col]*vec[1];
		cross[1] = data[dim*2 + col]*vec[0] - data[col]*vec[2];
		cross[2] = data[col]*vec[1] - data[dim + col]*vec[0];
		return cross;
	}
	/**
	 * dot product of two vectors. calculated by summing the product of each index. ie: ax1*bx1 + ax2*bx2 + ...
	 * @param a - first vector
	 * @param b - second vector
	 * @returns the product.
	 */
	public static float dot(float[] a, float[] b)
	{
		float dot = 0;
		for(int i = 0; i < a.length; i++)
			dot += a[i]*b[i];
		return dot;
	}
}

