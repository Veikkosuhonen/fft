#ifdef GL_ES
precision mediump float;
#endif

uniform vec2 u_resolution;

void main()
{
    gl_FragColor = vec4(gl_FragCoord/u_resolution, 1.0, 1.0);
}