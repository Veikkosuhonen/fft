#ifdef GL_ES
precision mediump float;
#endif

void main()
{
    gl_FragColor = vec4(gl_FragCoord.x/420.0, gl_FragCoord.y/420.0, 1.0, 1.0);
}