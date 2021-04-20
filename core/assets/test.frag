//#version 120 core

const int BUFFER_SIZE = 510;

uniform float u_time;
uniform vec2 u_resolution;
uniform float u_freq[BUFFER_SIZE * 2];

vec3 hsv2rgb(vec3 c) {
    vec4 K = vec4(1.0, 2.0 / 3.0, 1.0 / 3.0, 3.0);
    vec3 p = abs(fract(c.xxx + K.xyz) * 6.0 - K.www);
    return c.z * mix(K.xxx, clamp(p - K.xxx, 0.0, 1.0), c.y);
}

float getLFrequency(float x) {
    if (x >= 1.0 || x <= 0.0) return 0.0;
    int index = int(x * float(BUFFER_SIZE));
    float amp = u_freq[index];
    return log(amp + 1.0) / 3.0;
}

float getRFrequency(float x) {
    if (x >= 1.0 || x <= 0.0) return 0.0;
    int index = int(x * float(BUFFER_SIZE) + float(BUFFER_SIZE));
    float amp = u_freq[index];
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

    float s = (ampL + ampR);
    vec3 col = hsv2rgb(vec3(sin(u_time / 10.0 + fragPos.x) * 0.5 + 0.5, s + 0.2, s));
    gl_FragColor = vec4(a * col, 1.0);
}