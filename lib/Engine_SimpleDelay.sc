Engine_SimpleDelay : CroneEngine {
  //var <responder; 
  //var <onsetdetector;
  var pg;
  var synth;
  var recorder;
  var buffer1;
  var buffer2;
  var bufnum1=12;
  var bufnum2=13;

  *new { arg context, doneCallback;
    ^super.new(context, doneCallback);
  }

  alloc { 
    pg = ParGroup.tail(context.xg);
    buffer1 = Buffer.alloc(context.server,48000 * 0.1, 1,bufnum:bufnum1); 
    buffer2 = Buffer.alloc(context.server,48000 * 0.1, 1,bufnum:bufnum2); 
    synth = { arg delay=0.001, volume=0.0;
      var input = SoundIn.ar([0, 1]);
      BufDelayN.ar([bufnum1,bufnum2], input, delayTime:0.1, mul:volume)
    }.play(context.server);

    // onset detection!
    // onsetdetector = { arg onset_threshold=0.1, onsets_delay = 0, volume=0;
    //   var input = SoundIn.ar([0, 1]);
    //   var chain = FFT(LocalBuf(512), input);
    //   var onsets = Onsets.kr(chain, onset_threshold, \rcomplex, floor: 0.2,mingap:20);
    //   SendTrig.kr(onsets, 0, 1);
    // }.play(context.server);
    // responder = OSCFunc(
    //     { 
    //       arg msg, time; 
    //       // postln('['++time++']');
    //       NetAddr("127.0.0.1", 10111).sendMsg("onset",1,time); 
    //     },'/tr', context.server.addr);

    this.addCommand("delay", "f", { arg msg; synth.set(\delay, msg[1]); });
    this.addCommand("volume", "f", { arg msg; synth.set(\volume, msg[1]);});
  }

  free {
    // responder.free;
    // onsetdetector.free;
    pg.free;
    synth.free;
    recorder.free;
    buffer1.free;
    buffer2.free;
  }
} 