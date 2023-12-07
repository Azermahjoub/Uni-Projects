import numpy as np


####################################################################################################
# Exercise 1: DFT

def dft_matrix(n: int) -> np.ndarray:
    """
    Construct DFT matrix of size n.

    Arguments:
    n: size of DFT matrix

    Return:
    F: DFT matrix of size n

    Forbidden:
    - numpy.fft.*
    """
    # TODO: initialize matrix with proper size
    F = np.zeros((n, n), dtype='complex128')

    # TODO: create principal term for DFT matrix
    om = -2 * np.pi * 1j / n
    om = np.exp(om)
    # TODO: fill matrix with values
    for i in range(n):
        for j in range(n):
            F[i][j] = om ** (j * i)
    # TODO: normalize dft matrix
    F = F / np.sqrt(n)
    return F


def is_unitary(matrix: np.ndarray) -> bool:
    """
    Check if the passed in matrix of size (n times n) is unitary.

    Arguments:
    matrix: the matrix which is checked

    Return:
    unitary: True if the matrix is unitary
    """
    unitary = True
    # TODO: check that F is unitary, if not return false
    product = np.dot(matrix.T.conj(), matrix)
    unitary = np.allclose(np.eye(len(matrix)), product)
    return unitary


def create_harmonics(n: int = 128) -> (list, list):
    """
    Create delta impulse signals and perform the fourier transform on each signal.

    Arguments:
    n: the length of each signal

    Return:
    sigs:  list of np.ndarrays that store the delta impulse signals
    fsigs: list of np.ndarrays with the fourier transforms of the signals
    """

    # list to store input signals to DFT
    sigs = []
    # Fourier-transformed signals
    fsigs = []

    # TODO: create signals and extract harmonics out of DFT matrix
    for i in range(n):
        s = np.zeros(n)
        s[i] = 1
        sigs.append(s)
        p = np.dot(dft_matrix(n), s)
        fsigs.append(p)

    return sigs, fsigs


####################################################################################################
# Exercise 2: FFT

def shuffle_bit_reversed_order(data: np.ndarray) -> np.ndarray:
    """
    Shuffle elements of data using bit reversal of list index.

    Arguments:
    data: data to be transformed (shape=(n,), dtype='float64')

    Return:
    data: shuffled data array
    """

    # TODO: implement shuffling by reversing index bits
    another = data
    for i in range(data.size):
        x = int(bin(i)[2:].zfill(int(np.log2(data.size)))[::-1], 2)
        if i < x:
            bet = another[i]
            another[i] = another[x]
            another[x] = bet
    data = another
    return data


def fft(data: np.ndarray) -> np.ndarray:
    """
    Perform real-valued discrete Fourier transform of data using fast Fourier transform.

    Arguments:
    data: data to be transformed (shape=(n,), dtype='float64')

    Return:
    fdata: Fourier transformed data

    Note:
    This is not an optimized implementation but one to demonstrate the essential ideas
    of the fast Fourier transform.

    Forbidden:
    - numpy.fft.*
    """

    fdata = np.asarray(data, dtype='complex128')
    n = fdata.size

    # check if input length is power of two
    if not n > 0 or (n & (n - 1)) != 0:
        raise ValueError

    # TODO: first step of FFT: shuffle data
    fdata = shuffle_bit_reversed_order(fdata)
    # TODO: second step, recursively merge transforms
    x = 1
    while n > x:
        for i in range(x):
            z = np.exp(-1j * 2 * np.pi * i / (x * 2))
            for j in range(0, n, x * 2):
                bet = fdata[j + i]
                fdata[j + i] = bet + z * fdata[j + i + x]
                fdata[j + i + x] = bet - z * fdata[j + i + x]
        x *= 2

    # TODO: normalize fft signal
    fdata /= np.sqrt(n)
    return fdata


def generate_tone(f: float = 261.626, num_samples: int = 44100) -> np.ndarray:
    """
    Generate tone of length 1s with frequency f (default mid C: f = 261.626 Hz) and return the signal.

    Arguments:
    f: frequency of the tone

    Return:
    data: the generated signal
    """

    # sampling range
    x_min = 0.0
    x_max = 2 * np.pi

    # TODO: Generate sine wave with proper frequency
    x = np.linspace(x_min, x_max, num_samples)
    data =  np.sin(x * f)
    return data


def low_pass_filter(adata: np.ndarray, bandlimit: int = 1000, sampling_rate: int = 44100) -> np.ndarray:
    """
    Filter high frequencies above bandlimit.

    Arguments:
    adata: data to be filtered
    bandlimit: bandlimit in Hz above which to cut off frequencies
    sampling_rate: sampling rate in samples/second

    Return:
    adata_filtered: filtered data
    """

    # translate bandlimit from Hz to dataindex according to sampling rate and data size
    bandlimit_index = int(bandlimit * adata.size / sampling_rate)

    # TODO: compute Fourier transform of input data
    ad = np.fft.ifft(adata)
    # TODO: set high frequencies above bandlimit to zero, make sure the almost symmetry of the transform is respected.
    for i in range(bandlimit_index + 1, len(ad) - bandlimit_index):
        ad[i] = 0
        ad[len(ad) - i] = 0
    # TODO: compute inverse transform and extract real component
    adata_filtered = np.real(np.fft.fft(ad))
    return adata_filtered


if __name__ == '__main__':
    print("All requested functions for the assignment have to be implemented in this file and uploaded to the "
          "server for the grading.\nTo test your implemented functions you can "
          "implement/run tests in the file tests.py (> python3 -v test.py [Tests.<test_function>]).")
