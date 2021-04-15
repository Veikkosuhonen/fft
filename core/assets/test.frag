//#version 120 core

const int BUFFER_SIZE = 256;

uniform float u_time;
uniform vec2 u_resolution;
uniform float u_freq[BUFFER_SIZE * 2];

float getLFrequency(float x) {
    if (x >= 1.0 || x <= 0.0) return 0.0;
    int index = int(x * float(BUFFER_SIZE));
    float amp = abs( u_freq[index] );
    return log(amp + 1.0) / 3.0;
}

float getRFrequency(float x) {
    if (x >= 1.0 || x <= 0.0) return 0.0;
    int index = int(x * float(BUFFER_SIZE) + float(BUFFER_SIZE));
    float amp = abs( u_freq[index] );
    return log(amp + 1.0) / 3.0;
}

void main() {
    //vec2 u_resolution = vec2(100., 100.);
    vec2 fragPos = gl_FragCoord.st/u_resolution;
    fragPos *= 2.0;
    fragPos -= 0.5;
    float ampL = getLFrequency(fragPos.x);
    float ampR = getRFrequency(fragPos.x);

    float a = step(0.5, ampL + 1.0 - fragPos.y) * (1. - step(0.5, (1. - fragPos.y) - ampR));
    a *= 1.0 - abs(fragPos.y - 0.5);
    vec3 col = vec3(cos(u_time / 5.46) / 2. + .5, sin(u_time / 7.111 + 1.) / 2. + .5, sin(u_time / 3.14) / 2. + .5);
    gl_FragColor = vec4(a * col, 1.0);
}