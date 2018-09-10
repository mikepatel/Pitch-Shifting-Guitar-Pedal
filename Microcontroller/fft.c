//
// Included Files

#include <stdio.h>
#include <math.h>
#include <complex.h>
#include "define.h"

//double PI;
//typedef double complex cplx;
extern cplx out[FFT_SIZE];
extern cplx buf[FFT_SIZE];
extern float min_freq,min_freq_mag;
int peak_found = 0;

void _fft(cplx buf[], cplx out[], int n, int step)
{
    int i;
    if (step < n) {
        _fft(out, buf, n, step * 2);
        _fft(out + step, buf + step, n, step * 2);

        for (i = 0; i < n; i += 2 * step) {
            cplx t = cexp(-I * PI * i / n) * out[i + step];
            buf[i / 2]     = out[i] + t;
            buf[(i + n)/2] = out[i] - t;
        }
    }
}

void fft(cplx input[], int n)
{
    int i;
    for (i = 0; i < n; i++) {
        buf[i]=input[i];
        out[i]=input[i];
    }

    _fft(buf, out, n, 1);
}

void init_wfm(cplx input[]){
    int i;
    int offset = 2048;
    int mag = 300;
    int freq = 116;
    float t;
    float omega = omega = 2*PI*freq;

    //Sine Wave
    for(i=0;i<FFT_SIZE/2;i++){
        t=i/(8e3);
        input[i] = offset+mag*cos(omega*t);
    }

    /*
     * Triangle Wave
    for(i=0;i<FFT_SIZE/2;i++){
        buf[i] = i;
    }
    for(i=FFT_SIZE/2;i<FFT_SIZE;i++){
            buf[i] = FFT_SIZE/2-(i-FFT_SIZE/2);
        }

    */
}

void find_freq(cplx buf[]){
    int i,j;
    //added peak found flag to prevent null noise
    peak_found = 0;
    float t1,t2;
    //min_freq = buf[1];
    for(i=2;i<FFT_SIZE/2;i++){
    //Looking for the maximum component of frequency spectrum
        t1 = buf[i];
        if(t1 > MAG_THRESH){
            peak_found = 1;
            t2 = buf[i+1];
            if(t1 > t2){
                j = i;
                min_freq = buf[i];
                break;
            }
        }
    }
    if(peak_found){
    min_freq = 8e3/1024 * (float)j;
    min_freq_mag = buf[j];
    }
}
