// main.c

#include "driverlib.h"
#include "device.h"
#include "define.h"
#include <math.h>

//GLOBALs
//#############################################################################
uint16_t index;
uint16_t fft_round[FFT_SIZE/2];
float min_freq,min_freq_mag;
volatile int detected_note_flag;
//#############################################################################
// map out RAM sections for FFT
#pragma DATA_SECTION(fft_in,"RFFTdata1")
//! \brief Magnitude Calculation Buffer
//!
cplx fft_in[FFT_SIZE];

#pragma DATA_SECTION(fft_out,"RFFTdata2")
//! \brief Magnitude Calculation Buffer
//!
cplx fft_out[FFT_SIZE];

#pragma DATA_SECTION(buf,"RFFTdata3")
//! \brief Magnitude Calculation Buffer
//!
cplx buf[FFT_SIZE];

#pragma DATA_SECTION(out,"RFFTdata4")
//! \brief Magnitude Calculation Buffer
//!
cplx out[FFT_SIZE];
//#############################################################################

//
// Main
void main(void){
    Device_init(); // Initialize device clock and peripherals
    Device_initGPIO(); // Disable pin locks and enable internal pullups.
    Interrupt_initModule(); // Initialize PIE and clear PIE registers. Disables CPU interrupts.
    Interrupt_initVectorTable(); // Initialize the PIE vector table with pointers to the shell ISR

    // Interrupts that are used in this example are re-mapped to ISR functions
    // found within this file.
    Interrupt_register(INT_ADCA1, &adcA1ISR);

    // Set up the ADC and the ePWM and initialize the SOC
    initADC();
    initEPWM();
    initADCSOC();
    initDAC();   // Set up the DAC
    clearADCbuffer(); // Initialize results buffer

    index = 0; // global index values for ADC buffer
    bufferFull = 0; // global ADC buffer full flag
    adcSum = 0;
    adcAvg = 0;
    detected_note_flag = 0;

    Interrupt_enable(INT_ADCA1); // Enable ADC interrupt

    EINT; // Enable Global Interrupt (INTM)
    ERTM; // Enable realtime interrupt (DBGM)
    ADCSample(); // indefinitely reads in ADC values

    // begin while-always loop
    while(ALWAYS){
        //testDAC();

        if(detected_note_flag){ //
                    //if(x==0)
                    fft(fft_in,FFT_SIZE);
                    //x=1;
                    for(i=0;i<FFT_SIZE/2;i++){
                        fft_round[i] = round(buf[i]);
                    }

                    find_freq(buf);//finds min frequency

                    bufferFull = 0; //indicates buffer is ready
                    detected_note_flag = 0;
        }
        //check oscillator flag and calculate next value if flag is high
        /*
               if(OSC_flag==1){
                   runOscillator();//calculate sinusoids
                   OSC_flag=0;

        }*/


        //ESTOP0; // Software breakpoint

    } // end of while-always loop
} // end of main()
