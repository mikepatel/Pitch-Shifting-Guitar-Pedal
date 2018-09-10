% modular/function representation of Alpha.m
% Matlab does not allow for multiple functions to be 
% contained in the same file
% inputs are strings

function new = generate(guitar, record);
    % PREPARE input and output files
    % guitar
    guitarName = strsplit(guitar, '.'); % split input string
    guitarName = guitarName(1); % name minus .wav
    guitarFile = char(guitar); % as char array instead of string
    
    % record
    recordName = strsplit(record, '.'); % split input string
    recordName = recordName(1); % name minus .wav
    recordedFile = char(record); % recorded as char array instead of string
    
    % new
    filename = strcat('new_', recordName, '_', guitarName, '.wav'); % new sound file
    filename = char(filename); % new as char array instead of string
    figureName = strcat('new_', recordName, '_', guitarName, '_plots.png'); % new figure plot
    figureName = char(figureName); % as char array instead of string
    
    % READ in sound files
    [g, Fsg] = audioread(guitarFile); % guitar
    [r, Fsr] = audioread(recordedFile); % recorded
    
    % FFT
    Gf = abs(fft(g(:,1))); % guitar
    Rf1 = abs(fft(r(:,1))); % channel 1
    Rf2 = abs(fft(r(:,2))); % channel 2
    
    % FUNDAMENTAL - pitch detection
    heightDivisor = 8;
    % guitar
    gHeight = max(Gf)/heightDivisor;
    [gPeaks, gFreqs] = findpeaks(Gf, 'MinPeakProminence', gHeight, 'WidthReference','halfheight');
    gfdisc = gFreqs(1); % discrete frequency
    
    % recorded channel 1
    rHeight1 = max(Rf1)/heightDivisor;
    [rPeaks1, rFreqs1] = findpeaks(Rf1, 'MinPeakProminence', rHeight1);
    rfdisc1 = rFreqs1(1); % discrete frequency
    
    % recorded channel 2
    rHeight2 = max(Rf2)/heightDivisor;
    [rPeaks2, rFreqs2] = findpeaks(Rf2, 'MinPeakProminence', rHeight2);
    rfdisc2 = rFreqs2(1); % discrete frequency
    
    % SUBSTITUTION
    width = round((Fsr/2) / rfdisc1); % channel 1
    
    for n=1:round(length(Rf1)/rfdisc1)-1 % channel 1
        Nf1( n*gfdisc-width : n*gfdisc+width ) = Rf1( n*rfdisc1-width : n*rfdisc1+width );
        Nf2( n*gfdisc-width : n*gfdisc+width ) = Rf2( n*rfdisc2-width : n*rfdisc2+width );
    end
    
    % AMPLIFY
    Nf1 = 5.*Nf1;
    Nf2 = 5.*Nf2;
    
    % IFFT
    new1 = real(ifft(Nf1));
    new2 = real(ifft(Nf2));
    new = [new1(:), new2(:)]; % combine channels
    
    % CUTOFF length
    cutoff = floor(min( length(Rf1), length(Nf1)*(length(Nf1)/length(Rf1)) ));
    new = new(1:cutoff, :); % resize duration of new sound
    
    % WRITE out new sound file
    audiowrite(filename, new, Fsr);
    
    % PLOTs
    fig = figure('visible', 'off');
    
    % time
    subplot(321);
    plot(g); % guitar
    grid on
    title('TIME: Guitar');
    xlabel('g(t)');

    subplot(323);
    plot(r); % recorded sound
    grid on
    title('TIME: Recorded Sound');
    xlabel('r(t)');

    subplot(325);
    plot(new); % new sound
    grid on
    title('TIME: New Sound');
    xlabel('n(t)');

    % discrete frequency
    subplot(322);
    plot(Gf, 'LineWidth', 1.5); % guitar
    grid on
    title('FREQUENCY: Guitar');
    axis([0 8e3 0 1.25*max(gPeaks)]);
    xlabel('G(k)');

    subplot(324);
    plot(Rf1, 'LineWidth', 1.5); % recorded sound
    grid on
    title('FREQUENCY: Recorded Sound');
    axis([0 8e3 0 1.25*max(Rf1)]);
    xlabel('R(k)');

    subplot(326);
    plot(Nf1, 'LineWidth', 1.5); % new sound
    grid on
    title('FREQUENCY: New Sound');
    axis([0 8e3 0 1.25*max(Nf1)]);
    xlabel('N(k)');
    
    saveas(fig, figureName);
end
