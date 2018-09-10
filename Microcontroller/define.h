//#############################################################################
//
// FILE:   define.h
//
// TITLE:  defines
//
//
//
// Contains Defines and function prototypes
//
//#############################################################################

#include <stdint.h>

//defines
//#############################################################################
//miscellaneous
#define ALWAYS              1                     // main loop variable
#define ONE_SECOND  1000000 // 1s of time

//ADC
#define ADC_BUFFER_SIZE     1024                   //ADC buffer size
#define ADC_RESOLUTION      ADC_RESOLUTION_12BIT  //adc 12bit resulution
#define ADC_SIGNAL_MODE     ADC_MODE_SINGLE_ENDED //single pin adc mode
#define ADC_COMPARE_A_VALUE 0x0001 //used in counter to trigger ADC
#define ADC_PERIOD  0x0BFF //8k sample frequency=0x0BFF
#define ADC_AVERAGE_SIZE 16
#define ADC_LOWER_BOUND 2057
#define ADC_UPPER_BOUND 2065

//DAC
#define DAC_BUFFER_SIZE 512
#define DAC_OUTPUT_OFFSET 2048
#define DAC_BUFFER_MULTIPLE 50

//OSCILLATORS
#define BANK_SIZE 10
#define PI 3.14159265
#define DAC_SAMPLE_RATE 8000
#define FREQ_OFFSET 8141 //this is used to bring the sample rate of the DAC down to output correct frequencies
#define COUNT_MAX 65535

//FFT
typedef double _Complex cplx;
#define FFT_SIZE 1024
#define MAG_THRESH 2500
//#############################################################################



//external variables
//#############################################################################
// ADC
extern uint16_t adcAResults[ADC_BUFFER_SIZE];   // Buffer for results
extern uint16_t index;                          // Index for ADC
extern volatile uint16_t bufferFull;            // Flag to indicate buffer is full
extern volatile unsigned int avgCount;

// DAC
extern volatile uint16_t testDACvalues[4];   // DAC values for testing purposes
extern volatile uint16_t dacCvalues[DAC_BUFFER_SIZE];

// PITCH DETECTION
extern volatile double xcorr[ADC_BUFFER_SIZE];
extern volatile double location;
extern volatile int locCount;
extern volatile double locs[ADC_BUFFER_SIZE];
extern volatile double fundamental;

//FFT
extern cplx fft_in[FFT_SIZE];
extern float min_freq,min_freq_mag;

//OSCILLATORS
extern double Freqs[BANK_SIZE];
extern double Amps[BANK_SIZE];
extern double OSC; //oscillator value
extern double PHASE0;
extern double PHASE1;
extern double PHASE2;
extern double PHASE3;
extern double PHASE4;
extern double PHASE5;
extern double PHASE6;
extern double PHASE7;
extern double PHASE8;
extern double PHASE9;
extern unsigned int i;//this value keeps track of the oscillators output position
extern volatile unsigned int OSC_flag;// this value indicates whether or not to calculate a new oscillator value.
extern double attenuation;
extern double K;
extern int peak_found;
extern double adcSum;
extern double adcAvg;
extern volatile int detected_note_flag;
//#############################################################################



//function prototypes
//#############################################################################
void ADCSample();                  //run adc
void initADC(void);
void initEPWM(void);
void initADCSOC(void);
__interrupt void adcA1ISR(void);
void clearADCbuffer();

// DAC
void initDAC(void);
void setDACvalue(unsigned int);
void testDAC(void);

// PITCH DETECTION
void initializePitchDetection();
void testPitchDetection();
double* autocorr(int[], int);
double getLocation(double[], int);
double* getDiffs(double[], int);
double getAvg(double[], int);
double getFundamental(double[], int);

//OSCILLATOR
void runOscillator(void);
void createAttenuation(void);
void sineTest(void);

//FFT
void _fft(cplx buf[], cplx out[], int n, int step);
void fft(cplx buf[], int n);
void init_buf(cplx buf[]);
void find_freq(cplx buf[]);
//#############################################################################
