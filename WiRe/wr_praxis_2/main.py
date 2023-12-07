import numpy as np
import tomograph


####################################################################################################
# Exercise 1: Gaussian elimination

def gaussian_elimination(A: np.ndarray, b: np.ndarray, use_pivoting: bool = True) -> (np.ndarray, np.ndarray):
    """
    Gaussian Elimination of Ax=b with or without pivoting.

    Arguments:
    A : matrix, representing left side of equation system of size: (m,m)
    b : vector, representing right hand side of size: (m, )
    use_pivoting : flag if pivoting should be used

    Return:
    A : reduced result matrix in row echelon form (type: np.ndarray, size: (m,m))
    b : result vector in row echelon form (type: np.ndarray, size: (m, ))

    Raised Exceptions:
    ValueError: if matrix and vector sizes are incompatible, matrix is not square or pivoting is disabled but necessary

    Side Effects:
    -

    Forbidden:
    - numpy.linalg.*
    """
    # Create copies of input matrix and vector to leave them unmodified

    A = A.copy()
    b = b.copy()

    # TODO: Test if shape of matrix and vector is compatible and raise ValueError if not
    m_A, n_A = np.shape(A)
    m_b = np.shape(b)
    if m_A != n_A:
        raise ValueError("matrix is not square")
    if m_A != m_b[0]:
        raise ValueError("matrix and vector sizes are incompatible")
    for i in range(m_A):
        if A[i][i] == 0 and use_pivoting == False:
            raise ValueError("pivoting is disabled but necessary")
    # TODO: Perform gaussian elimination

    for i in range(m_A):
        if use_pivoting:
            for c in range(i + 1, m_A):
                if np.abs(A[c][i]) > np.abs(A[i][i]):
                    A[[i, c]] = A[[c, i]]
                    b[[i, c]] = b[[c, i]]
        for j in range(i + 1, m_A):
            if A[i, i] == 0:
                raise ValueError("No solution")
            sub_row = A[j][i] / A[i][i]
            A[j, :] -= sub_row * A[i, :]
            b[j] -= sub_row * b[i]
    return A, b


def back_substitution(A: np.ndarray, b: np.ndarray) -> np.ndarray:
    """
    Back substitution for the solution of a linear system in row echelon form.

    Arguments:
    A : matrix in row echelon representing linear system
    b : vector, representing right hand side

    Return:
    x : solution of the linear system

    Raised Exceptions:
    ValueError: if matrix/vector sizes are incompatible or no/infinite solutions exist

    Side Effects:
    -

    Forbidden:
    - numpy.linalg.*
    """

    # TODO: Test if shape of matrix and vector is compatible and raise ValueError if not
    m_A, n_A = np.shape(A)
    m_b = np.shape(b)
    if m_A != m_b[0]:
        raise ValueError("matrix and vector sizes are incompatible")
    # TODO: Initialize solution vector with proper size
    x = np.zeros(m_A)

    # TODO: Run backsubstitution and fill solution vector, raise ValueError if no/infinite solutions exist
    if m_A != n_A:
        raise ValueError("no/infinite solutions exist")
    for i in range(m_A):
        if A[i][i] == 0:
            raise ValueError("no/infinite solutions exist")
    x[m_A-1] = b[m_A-1]/A[m_A-1,m_A-1]
    for i in range(m_A - 2, -1, -1):
        sum = 0
        for j in range(i+1, m_A):
            sum += A[i, j] * x[j]
        x[i] = (1 /A[i, i])*(b[i] - sum)
    return x

def forward_substitution(A: np.ndarray, b: np.ndarray) -> np.ndarray:
    m_A, n_A = np.shape(A)
    m_b = np.shape(b)
    if m_A != m_b[0]:
        raise ValueError("matrix and vector sizes are incompatible")
    x = np.zeros(m_A)
    if m_A != n_A:
        raise ValueError("no/infinite solutions exist")
    for i in range(m_A):
        if A[i][i] == 0:
            raise ValueError("no/infinite solutions exist")
    x[0] = b[0]/A[0,0]
    for i in range(1,m_A):
        sum = 0
        for j in range(i):
            sum += A[i, j] * x[j]
        x[i] = (1 /A[i, i])*(b[i] - sum)
    return x
####################################################################################################
# Exercise 2: Cholesky decomposition

def compute_cholesky(M: np.ndarray) -> np.ndarray:
    """
    Compute Cholesky decomposition of a matrix

    Arguments:
    M : matrix, symmetric and positive (semi-)definite

    Raised Exceptions:
    ValueError: L is not symmetric and psd

    Return:
    L :  Cholesky factor of M

    Forbidden:
    - numpy.linalg.*
    """

    # TODO check for symmetry and raise an exception of type ValueError
    (n, m) = M.shape
    if m != n:
        raise ValueError("Matrix is not a square one")
    M_T = np.transpose(M)
    for i in range(n):
        for j in range(n):
            if not np.allclose(M[i,j],M[i,j]):
                raise ValueError("Matrix is not symetric")

    # TODO build the factorization and raise a ValueError in case of a non-positive definite input matrix

    L = np.zeros((n, n))
    for i in range (n):
        for j in range (i+1):
            sum = 0
            for k in range(j):
                sum+= L[j,k]*L[i,k]
            if i==j:
                elem = M[i,i] - sum
                if elem < 0 :
                    raise ValueError("not pd")
                L[i,i] = elem**0.5
            else:
                L[i,j] = (M[i,j]-sum)/L[j,j]

    return L


def solve_cholesky(L: np.ndarray, b: np.ndarray) -> np.ndarray:
    """
    Solve the system L L^T x = b where L is a lower triangular matrix

    Arguments:
    L : matrix representing the Cholesky factor
    b : right hand side of the linear system

    Raised Exceptions:
    ValueError: sizes of L, b do not match
    ValueError: L is not lower triangular matrix

    Return:
    x : solution of the linear system

    Forbidden:
    - numpy.linalg.*
    """

    # TODO Check the input for validity, raising a ValueError if this is not the case
    (n, m) = L.shape
    x = b.shape
    if not(n==m):
        raise ValueError("nicht quadratisch")
    if not(n==x[0]):
        raise ValueError("nicht kompatibel")
    if not np.allclose(L, np.tril(L)):
        raise ValueError ()

    # TODO Solve the system by forward- and backsubstitution
    x = np.zeros(m)
    y = np.zeros(m)
    L_T = np.transpose(L)
    y = forward_substitution(L,b)
    x = back_substitution(L_T,y)


    return x


####################################################################################################
# Exercise 3: Tomography

def setup_system_tomograph(n_shots: np.int64, n_rays: np.int64, n_grid: np.int64) -> (np.ndarray, np.ndarray):
    """
    Set up the linear system describing the tomographic reconstruction

    Arguments:
    n_shots : number of different shot directions
    n_rays  : number of parallel rays per direction
    n_grid  : number of cells of grid in each direction, in total n_grid*n_grid cells

    Return:
    L : system matrix
    g : measured intensities

    Raised Exceptions:
    -

    Side Effects:
    -

    Forbidden:
    -
    """

    # TODO: Initialize system matrix with proper size
    L = np.zeros((n_rays*n_shots,n_grid*n_grid ))
    # TODO: Initialize intensity vector
    g = np.zeros(n_rays*n_shots)

    # TODO: Iterate over equispaced angles, take measurements, and update system matrix and sinogram
    theta = 0
    # Take a measurement with the tomograph from direction r_theta.
    # intensities: measured intensities for all <n_rays> rays of the measurement. intensities[n] contains the intensity for the n-th ray
    # ray_indices: indices of rays that intersect a cell
    # isect_indices: indices of intersected cells
    # lengths: lengths of segments in intersected cells
    # The tuple (ray_indices[n], isect_indices[n], lengths[n]) stores which ray has intersected which cell with which length. n runs from 0 to the amount of ray/cell intersections (-1) of this measurement.
    free_g = 0
    for m in range(n_shots):
        theta = m *(np.pi/n_shots)
        intensities, ray_indices, isect_indices, lengths = tomograph.take_measurement(n_grid, n_rays, theta)
        for i in range(n_rays):
            g[i+free_g]=intensities[i]
        n = len(ray_indices)
        for i in range(n):
            L[free_g+ray_indices[i],isect_indices[i]] = lengths[i]
        free_g += (n_rays)

    return [L, g]


def compute_tomograph(n_shots: np.int64, n_rays: np.int64, n_grid: np.int64) -> np.ndarray:
    """
    Compute tomographic image

    Arguments:
    n_shots : number of different shot directions
    n_rays  : number of parallel rays per direction
    n_grid  : number of cells of grid in each direction, in total n_grid*n_grid cells

    Return:
    tim : tomographic image

    Raised Exceptions:
    -

    Side Effects:
    -

    Forbidden:
    """

    # Setup the system describing the image reconstruction
    [L, g] = setup_system_tomograph(n_shots, n_rays, n_grid)
    L_side = np.dot(np.transpose(L),L)
    g_side = np.dot(np.transpose(L),g)
    solution = solve_cholesky(compute_cholesky(L_side),g_side)

    # TODO: Solve for tomographic image using your Cholesky solver
    # (alternatively use Numpy's Cholesky implementation)
    [L, g] = setup_system_tomograph(n_shots, n_rays, n_grid)
    lösung = np.linalg.solve(np.dot(np.transpose(L), L), np.dot(np.transpose(L), g))
    # TODO: Convert solution of linear system to 2D image
    tim = np.zeros((n_grid, n_grid))
    k = 0
    for i in range(n_grid):
        for j in range(n_grid):
            tim[i, j] = lösung[i * n_grid + j]
            k += 1

    return tim


if __name__ == '__main__':
    print("All requested functions for the assignment have to be implemented in this file and uploaded to the "
          "server for the grading.\nTo test your implemented functions you can "
          "implement/run tests in the file tests.py (> python3 -v test.py [Tests.<test_function>]).")
