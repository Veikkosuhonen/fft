#version 330 core

attribute vec4 a_position;

void main()
{
    gl_Position = a_position;
}