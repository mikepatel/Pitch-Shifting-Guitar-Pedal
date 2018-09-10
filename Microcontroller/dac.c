
//#############################################################################
//
// FILE:   dac.c
//
// TITLE:  DAC
//
//
//
// Contains DAC functions
// generate voltage on buffered DAC output: DACCOUTC aka ADCINB1 (J3 pin 24)
// set external reference voltage (VDAC pin - ADCINB0 (J3 pin 28) ) to 3.3V
// DAC is 12-bit
// using DAC C (ADC B)
//#############################################################################



//included files
//#############################################################################
#include "driverlib.h"
#include "device.h"
#include "define.h"
//#############################################################################



//#############################################################################
// GLOBALs
// values for testing DAC only
volatile uint16_t testDACvalues[4] = {869, 1241, 2482, 2793};   // 869 = 0.7V
                                                                // 1241 = 1V
                                                                //2482 = 2V
                                                                //2793 = 2.25V
//#############################################################################



//functions
//#############################################################################
//init the DAC
void initDAC(){
    DAC_setReferenceVoltage(DACC_BASE, DAC_REF_VDAC);   // Set VDAC as the DAC reference voltage.
                                                        // (Edit here to use ADC VREF as the reference voltage)

    DAC_enableOutput(DACC_BASE); // Enable the DAC output
    DAC_setShadowValue(DACC_BASE, 0); // Set the DAC shadow output to 0
    DEVICE_DELAY_US(10); // Delay 10 microseconds for buffered DAC to power up
}



//send value to the dac
void setDACvalue(unsigned int val){
    DAC_setShadowValue(DACC_BASE, val);
}



//dac test function, only used to test that the dac is working
void testDAC(){
    int i=0;

    for(i=0; i<sizeof(testDACvalues); i++){
        setDACvalue(testDACvalues[i]);
        DEVICE_DELAY_US(ONE_SECOND);
    }

}
//#############################################################################


