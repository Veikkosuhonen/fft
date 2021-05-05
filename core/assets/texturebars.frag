//#version 120 core

uniform vec2 u_resolution;

uniform sampler2D spectrogram;

vec4 colormap(float x);
vec4 getSpectrogram(vec2 st);
vec4 getBars(vec2 st);

void main() {
    vec2 st = gl_FragCoord.st / u_resolution;
    vec4 color = vec4(0.0);
    color += getSpectrogram(st);
    color += getBars(st);
    gl_FragColor = color;
}

vec4 getSpectrogram(vec2 st) {
    st *= vec2(1.2, 1.2); // zoom out a bit
    st -= vec2(0.2, 0.1);
    float a = texture2D(spectrogram, st).r; // get the frequencyy
    a *= step(-st.x, 0.0) * step(-st.y, 0.0) /** step(st.x, 1.0)*/ * step(st.y, 1.0); // make it zero outside the texture
    return colormap(a - 0.2);
}

vec4 getBars(vec2 st) {
    st *= vec2(6.0, 1.2);
    st.y -= 0.1;
    st.x = 1.0 - st.x;
    float a = texture2D(spectrogram, vec2(0.0, st.y)).r; // get the frequencyy
    float height = a;
    a *= step(st.x, height);
    a *= step(-st.x, 0.0) * step(-st.y, 0.0) * /*step(st.x, 1.0) **/ step(st.y, 1.0); // make it zero outside the bar area
    return vec4(min(1.0, 2.0 * a), 0.0, a, 1.0);
}

// Colormap from https://github.com/kbinani/colormap-shaders/blob/master/shaders/glsl/IDL_CB-Spectral.frag
float colormap_red(float x) {
    if (x < 0.09752005946586478) {
        return 5.63203907203907E+02 * x + 1.57952380952381E+02;
    } else if (x < 0.2005235116443438) {
        return 3.02650769230760E+02 * x + 1.83361538461540E+02;
    } else if (x < 0.2974133397506856) {
        return 9.21045429665647E+01 * x + 2.25581007115501E+02;
    } else if (x < 0.5003919130598823) {
        return 9.84288115246108E+00 * x + 2.50046722689075E+02;
    } else if (x < 0.5989021956920624) {
        return -2.48619704433547E+02 * x + 3.79379310344861E+02;
    } else if (x < 0.902860552072525) {
        return ((2.76764884219295E+03 * x - 6.08393126459837E+03) * x + 3.80008072407485E+03) * x - 4.57725185424742E+02;
    } else {
        return 4.27603478260530E+02 * x - 3.35293188405479E+02;
    }
}

float colormap_green(float x) {
    if (x < 0.09785836420571035) {
        return 6.23754529914529E+02 * x + 7.26495726495790E-01;
    } else if (x < 0.2034012006283468) {
        return 4.60453201970444E+02 * x + 1.67068965517242E+01;
    } else if (x < 0.302409765476316) {
        return 6.61789401709441E+02 * x - 2.42451282051364E+01;
    } else if (x < 0.4005965758690823) {
        return 4.82379130434784E+02 * x + 3.00102898550747E+01;
    } else if (x < 0.4981907026473237) {
        return 3.24710622710631E+02 * x + 9.31717541717582E+01;
    } else if (x < 0.6064345916502067) {
        return -9.64699507389807E+01 * x + 3.03000000000023E+02;
    } else if (x < 0.7987472620841592) {
        return -2.54022986425337E+02 * x + 3.98545610859729E+02;
    } else {
        return -5.71281628959223E+02 * x + 6.51955082956207E+02;
    }
}

float colormap_blue(float x) {
    if (x < 0.0997359608740309) {
        return 1.26522393162393E+02 * x + 6.65042735042735E+01;
    } else if (x < 0.1983790695667267) {
        return -1.22037851037851E+02 * x + 9.12946682946686E+01;
    } else if (x < 0.4997643530368805) {
        return (5.39336225400169E+02 * x + 3.55461986381562E+01) * x + 3.88081126069087E+01;
    } else if (x < 0.6025972254407099) {
        return -3.79294261294313E+02 * x + 3.80837606837633E+02;
    } else if (x < 0.6990141388105746) {
        return 1.15990231990252E+02 * x + 8.23805453805459E+01;
    } else if (x < 0.8032653181119567) {
        return 1.68464957265204E+01 * x + 1.51683418803401E+02;
    } else if (x < 0.9035796343050095) {
        return 2.40199023199020E+02 * x - 2.77279202279061E+01;
    } else {
        return -2.78813846153774E+02 * x + 4.41241538461485E+02;
    }
}

vec4 colormap(float x) {
    float r = clamp(colormap_red(x) / 255.0, 0.0, 1.0);
    float g = clamp(colormap_green(x) / 255.0, 0.0, 1.0);
    float b = clamp(colormap_blue(x) / 255.0, 0.0, 1.0);
    return vec4(r, g, b, 1.0);
}