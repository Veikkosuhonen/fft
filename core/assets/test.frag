#version 330 core

const int BUFFER_SIZE = 256;

uniform float u_time;
uniform vec2 u_resolution;
uniform float u_freq[BUFFER_SIZE * 2];

float getLFrequency(float x) {
    int index = int(x * BUFFER_SIZE);
    return abs( u_freq[index] ) / 10.0;
}

float getRFrequency(float x) {
    int index = int(x * BUFFER_SIZE + BUFFER_SIZE);
    return abs( u_freq[index] ) / 10.0;
}

void main()
{
    //vec2 u_resolution = vec2(100., 100.);
    vec2 fragPos = gl_FragCoord.st/u_resolution;
    float ampL = getLFrequency(fragPos.x);
    float ampR = getRFrequency(fragPos.x);

    float a = step(0.5, ampL + 1.0 - fragPos.y) * (1. - step(0.5, (1. - fragPos.y) - ampR));
    a *= 2.0 - abs(fragPos.y - 0.5) * 2.0;
    gl_FragColor = vec4(a * vec3(cos(u_time / 5.46) / 2. + .5, sin(u_time / 7.111 + 1.) / 2. + .5, sin(u_time / 3.14) / 2. + .5), 1.0);
}