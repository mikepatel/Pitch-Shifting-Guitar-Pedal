//#############################################################################
//
// FILE:   oscillator.c
//
// TITLE:  Oscillators
//
//
//
// Contains oscillator functions
//
//#############################################################################



//included files
//#############################################################################
#include "driverlib.h"
#include "device.h"
#include "define.h"
#include "math.h"
#include <stdio.h>
//#############################################################################



//globals
//#############################################################################
double Freqs[BANK_SIZE]  = {665.37, 1332, 2007.1, 2688.7, 3333.3, 4023.4, 4656.9, 5327.5, 5993.5, 6656.9, 7348.3, 8654.3}; // right now, these are piano freqs
double Amps[BANK_SIZE] = {1, 0.14519, 0.061716, 0.030472, 0.018082, 0.0062476, 0.003939, 0.0019909, 0.0026463, 0.001922, 0.00081085, 0.00095995}; // normalized piano amps
double attenuation;//attenuation array
//values for tracking phase
double PHASE0;
double PHASE1;
double PHASE2;
double PHASE3;
double PHASE4;
double PHASE5;
double PHASE6;
double PHASE7;
double PHASE8;
double PHASE9;
double OSC = {0.0};
double K=0;
unsigned int i=0;//this value keeps track of the oscillators output position
volatile unsigned int OSC_flag=1;// this value indicates whether or not to calculate a new oscillator value. this should usually be initialized to 1 after the first fft calculation occurs
//#############################################################################

float mag=1000;
//functions
//#############################################################################
//calculate sinusoidal sum
void runOscillator(){
         OSC = attenuation * mag * (Amps[0]*sin((2*PI*min_freq*i/(FREQ_OFFSET)) + PHASE0) + Amps[1]*sin((4.0043*PI*min_freq*i/(FREQ_OFFSET)) + PHASE1) + Amps[2]*sin((6.0068*PI*min_freq*i/(FREQ_OFFSET)) + PHASE2) + Amps[3]*sin((7.9955*PI*min_freq*i/(FREQ_OFFSET)) + PHASE3)+ Amps[4]*sin((10.0026*PI*min_freq*i/(FREQ_OFFSET)) + PHASE4)+ Amps[5]*sin((12.007*PI*min_freq*i/(FREQ_OFFSET)) + PHASE5)+ Amps[6]*sin((13.00975*PI*min_freq*i/(FREQ_OFFSET)) + PHASE6)+ Amps[7]*sin((16.0014*PI*min_freq*i/(FREQ_OFFSET)) + PHASE7)+ Amps[8]*sin((17.9968*PI*min_freq*i/(FREQ_OFFSET)) + PHASE8)+ Amps[9]*sin((20.0072*PI*min_freq*i/(FREQ_OFFSET)) + PHASE9));
//       OSC = mag * (Amps[0]*sin((2*PI*Freqs[0]*i/(FREQ_OFFSET)) + PHASE0) + Amps[1]*sin((2*PI*Freqs[1]*i/(FREQ_OFFSET)) + PHASE1) + Amps[2]*sin((2*PI*Freqs[2]*i/(FREQ_OFFSET)) + PHASE2) + Amps[3]*sin((2*PI*Freqs[3]*i/(FREQ_OFFSET)) + PHASE3)+ Amps[4]*sin((2*PI*Freqs[4]*i/(FREQ_OFFSET)) + PHASE4)+ Amps[5]*sin((2*PI*Freqs[5]*i/(FREQ_OFFSET)) + PHASE5)+ Amps[6]*sin((2*PI*Freqs[6]*i/(FREQ_OFFSET)) + PHASE6)+ Amps[7]*sin((2*PI*Freqs[7]*i/(FREQ_OFFSET)) + PHASE7)+ Amps[8]*sin((2*PI*Freqs[8]*i/(FREQ_OFFSET)) + PHASE8)+ Amps[9]*sin((2*PI*Freqs[9]*i/(FREQ_OFFSET)) + PHASE9)); //this is Oscillators 0-9
       i++;
    if(i==COUNT_MAX){
        //Block for keeping track of phase
        PHASE0 = (2*PI*min_freq*i/(FREQ_OFFSET) /(2*PI));
        PHASE1 = (4*PI*min_freq*i/(FREQ_OFFSET) /(2*PI));
        PHASE2 = (6*PI*min_freq*i/(FREQ_OFFSET) /(2*PI));
        PHASE3 = (8*PI*min_freq*i/(FREQ_OFFSET) /(2*PI));
        PHASE4 = (10*PI*min_freq*i/(FREQ_OFFSET) /(2*PI));
        PHASE5 = (12*PI*min_freq*i/(FREQ_OFFSET) /(2*PI));
        PHASE6 = (14*PI*min_freq*i/(FREQ_OFFSET) /(2*PI));
        PHASE7 = (16*PI*min_freq*i/(FREQ_OFFSET) /(2*PI));
        PHASE8 = (18*PI*min_freq*i/(FREQ_OFFSET) /(2*PI));
        PHASE9 = (20*PI*min_freq*i/(FREQ_OFFSET) /(2*PI));
        i=0;
        }


//ESTOP0;
}

void sineTest(){
    unsigned int i;
    for(i=0;i<DAC_BUFFER_SIZE;i++){
        //OSC0[i] = mag * sin((2*PI*Freqs[0]*i/(FREQ_OFFSET)) + PHASE0);
        //setDACvalue(OSC0[i] + DAC_OUTPUT_OFFSET);
    }
    //PHASE0 = (2*PI*Freqs[0]*i/(FREQ_OFFSET) + PHASE0);
}

//creates decaying exponential for attentuation
void createAttenuation(){
    if(peak_found==1){ //when a new pitch is found the attenuation is restarted and we start looking for a new pitch
        K=0;
        peak_found=0;
    }
    if(K!=COUNT_MAX){ //stops the attenuation at the max to keep the signal low
        K++;
    }
    attenuation = (1/exp(K/8000));
}
//#############################################################################
