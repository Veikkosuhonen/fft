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
    return colormap(a - 0.3);
}

vec4 getBars(vec2 st) {
    st *= vec2(6.0, 1.2);
    st.y -= 0.1;
    st.x = 1.0 - st.x;
    float height = texture2D(spectrogram, vec2(0.0, st.y)).r; // get the frequencyy
    vec4 a = colormap(height - 0.3);
    a *= step(st.x, height);
    a *= step(-st.x, 0.0) * step(-st.y, 0.0) * /*step(st.x, 1.0) **/ step(st.y, 1.0); // make it zero outside the bar area
    return a;
}

// Colormap from https://github.com/kbinani/colormap-shaders
float colormap_red(float x) {
    if (x < 0.09790863520700754) {
        return 5.14512820512820E+02 * x + 1.64641025641026E+02;
    } else if (x < 0.2001887081633112) {
        return 2.83195402298854E+02 * x + 1.87288998357964E+02;
    } else if (x < 0.3190117539655621) {
        return 9.27301587301214E+01 * x + 2.25417989417999E+02;
    } else if (x < 0.500517389125164) {
        return 255.0;
    } else if (x < 0.6068377196788788) {
        return -3.04674876847379E+02 * x + 4.07495073891681E+02;
    } else if (x < 0.9017468988895416) {
        return (1.55336390191951E+02 * x - 7.56394659038288E+02) * x + 6.24412733169483E+02;
    } else {
        return -1.88350769230735E+02 * x + 2.38492307692292E+02;
    }
}

float colormap_green(float x) {
    if (x < 0.09638568758964539) {
        return 4.81427692307692E+02 * x + 4.61538461538488E-01;
    } else if (x < 0.4987066686153412) {
        return ((((3.25545903568267E+04 * x - 4.24067109461319E+04) * x + 1.83751375886345E+04) * x - 3.19145329617892E+03) * x + 8.08315127034676E+02) * x - 1.44611527812961E+01;
    } else if (x < 0.6047312345537269) {
        return -1.18449917898218E+02 * x + 3.14234811165860E+02;
    } else if (x < 0.7067635953426361) {
        return -2.70822112753102E+02 * x + 4.06379036672115E+02;
    } else {
        return (-4.62308723214883E+02 * x + 2.42936159122279E+02) * x + 2.74203431802418E+02;
    }
}

float colormap_blue(float x) {
    if (x < 0.09982818011951204) {
        return 1.64123076923076E+01 * x + 3.72646153846154E+01;
    } else if (x < 0.2958717460833126) {
        return 2.87014675052409E+02 * x + 1.02508735150248E+01;
    } else if (x < 0.4900527540014758) {
        return 4.65475113122167E+02 * x - 4.25505279034673E+01;
    } else if (x < 0.6017014681258838) {
        return 5.61032967032998E+02 * x - 8.93789173789407E+01;
    } else if (x < 0.7015737100463595) {
        return -1.51655677655728E+02 * x + 3.39446886446912E+02;
    } else if (x < 0.8237156500567735) {
        return -2.43405347593559E+02 * x + 4.03816042780725E+02;
    } else {
        return -3.00296889157305E+02 * x + 4.50678495922638E+02;
    }
}

vec4 colormap(float x) {
    float r = clamp(colormap_red(x) / 255.0, 0.0, 1.0);
    float g = clamp(colormap_green(x) / 255.0, 0.0, 1.0);
    float b = clamp(colormap_blue(x) / 255.0, 0.0, 1.0);
    return vec4(r, g, b, 1.0);
}