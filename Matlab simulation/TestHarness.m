% Automated Testing Harness for generating new .wav files and plots


clear all; close all;

guitars = ["GuitarA2.wav", "GuitarB3.wav", "GuitarD3.wav", "GuitarE4.wav"];
recorded = ["bark.wav", "clarinet_C4.wav", "Cminor_chord.wav",...
    "coin.wav", "flute_E2.wav", "growl.wav", "gunshot.wav",...
    "harmonica_E7.wav", "mario_growl.wav", "meow.wav",...
    "minijump.wav", "PianoE.wav", "PianoF.wav", "Police_Siren.wav",...
    "quack.wav", "sax_A2.wav", "sax_F#5.wav", "sonic_jump.wav",...
    "theremin.wav", "trumpet_A#3.wav", "Tuba_E#2.wav", "xfiles.wav"];

for i=1:length(guitars)
    for j=1:length(recorded)
        try
            new = generate(guitars(i), recorded(j));
        catch
            % NOTHING
        end
    end
end
