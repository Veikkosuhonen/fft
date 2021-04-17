// Credit to https://www.shadertoy.com/view/ls3BDH. Slightly adapted for this application
// Original: credit: https://www.shadertoy.com/view/4tGXzt
#version 330 core
precision highp float;

#define BEATMOVE 1

const int BUFFER_SIZE = 400;
const float FREQ_RANGE = BUFFER_SIZE;
const float PI = 3.1415;
const float RADIUS = 0.5;
const float BRIGHTNESS = 0.2;
const float SPEED = 0.5;

uniform float u_time;
uniform vec2 u_resolution;
uniform float u_freq[BUFFER_SIZE * 2]; //contains left (< BUFFER_SIZE) and right (> BUFFER_SIZE) channels

//convert HSV to RGB
vec3 hsv2rgb(vec3 c) {
    vec4 K = vec4(1.0, 2.0 / 3.0, 1.0 / 3.0, 3.0);
    vec3 p = abs(fract(c.xxx + K.xyz) * 6.0 - K.www);
    return c.z * mix(K.xxx, clamp(p - K.xxx, 0.0, 1.0), c.y);
}

float luma(vec3 color) {
    return dot(color, vec3(0.299, 0.587, 0.114));
}

/*float getFrequency(float x) {
    return texture(iChannel0, vec2(floor(x * FREQ_RANGE + 1.0) / FREQ_RANGE, 0.25)).x + 0.06;
}*/
float getFrequency(float x) {
    int index;
    if (x < 0.) {
        index = int(-x * BUFFER_SIZE + BUFFER_SIZE);
    } else {
        index = int(x * BUFFER_SIZE);
    }

    float amp = u_freq[index];
    return abs(amp);
}

float getFrequency_smooth(float x) {
    float index = floor(x * FREQ_RANGE) / FREQ_RANGE;
    float next = floor(x * FREQ_RANGE + 1.0) / FREQ_RANGE;
    return mix(getFrequency(index), getFrequency(next), smoothstep(0.0, 1.0, fract(x * FREQ_RANGE)));
}

float getFrequency_blend(float x) {
    return mix(getFrequency(x), getFrequency_smooth(x), 0.5);
}

vec3 doHalo(vec2 fragment, float radius) {
    float dist = length(fragment);
    float ring = 1.0 / abs(dist - radius);

    float b = dist < radius ? BRIGHTNESS * 0.3 : BRIGHTNESS;
    b *= (1. - dist * 0.5);

    vec3 col = vec3(0.0);

    float angle = atan(fragment.x, fragment.y);
    col += hsv2rgb( vec3( ( angle + u_time * 0.25 ) / (PI * 2.0), 1.0, 1.0 ) ) * ring * b;

    float frequency = max(getFrequency_blend(angle / PI) - 0.2, 0.0);
    col *= frequency;

    // Black halo
    col *= smoothstep(radius * 0.1, radius, dist);

    return col;
}

vec3 doLine(vec2 fragment, float radius, float x) {
    vec3 col = hsv2rgb(vec3(x * 0.23 + u_time * 0.12, 1.0, 1.0));

    float freq = fragment.x * 0.5;

    col *= (1.0 / abs(fragment.y)) * BRIGHTNESS * getFrequency(freq);
    col = col * smoothstep(radius, radius * 1.8, abs(fragment.x));

    return col;
}

void main() {
    vec2 fragPos = gl_FragCoord.st / u_resolution.xy;
    fragPos = (fragPos - 0.5) * 2.0;
    fragPos.x *= u_resolution.x / u_resolution.y;

    vec3 color = vec3(0.0134, 0.052, 0.1);
    color += doHalo(fragPos, RADIUS);

    float c = cos(u_time * SPEED);
    float s = sin(u_time * SPEED);
    vec2 rot = mat2(c,s,-s,c) * fragPos;
    color += doLine(rot, RADIUS, rot.x) * 0.7;

    color += max(luma(color) - 1.0, 0.0);

    gl_FragColor = vec4(color, 1.0);
}