#version 330 core

const int BUFFER_SIZE = 1000;

uniform float data[BUFFER_SIZE];
uniform vec2 u_resolution;

void main()
{
    vec2 fragPos = gl_FragCoord.st/u_resolution;
    int index = int(fragPos.x * BUFFER_SIZE);
    float y = data[index] * 2.0 + 1.0;
    float a = step(0.5, y - fragPos.y);
    gl_FragColor = vec4(vec3(a), 1.0);
}