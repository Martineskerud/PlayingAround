uniform float m_MinWavelength;
uniform float m_MaxWavelength;

varying vec2 texCoord1;

vec4 getColourFromWavelength(in float wavelength) {
float w = wavelength;
float red = 0.0;
float green = 0.0;
float blue = 0.0;
float alpha = 0.7;
float ic = 0.0;

// Colour
if(w >= 380.0 && w < 440.0) {
red = -(w – 440.0) / (440.0 – 350.0);
green = 0.0;
blue = 1.0;
} else if(w >= 440.0 && w < 490.0) {
red = 0.0;
green = (w – 440.0) / (490.0 – 440.0);
blue = 1.0;
} else if(w >= 490.0 && w < 510.0) {
red = 0.0;
green = 1.0;
blue = -(w – 510.0) / (510.0 – 490.0);
} else if(w >= 510.0 && w < 580.0) {
red = (w – 510.0) / (580.0 – 510.0);
green = 1.0;
blue = 0.0;
} else if(w >= 580.0 && w < 645.0) {
red = 1.0;
green = -(w – 645.0) / (645.0 – 580.0);
blue = 0.0;
} else if(w >= 645.0 && w <= 780.0) {
red = 1.0;
green = 0.0;
blue = 0.0;
} else if(w > 780.0) { // Infrared
red = 1.0;
green = 0.0;
blue = 0.0;
} else { // Ultraviolet
red = 1.0;
green = 0.0;
blue = 1.0;
}

// Intensity correction
if(w >= 380.0 && w < 420.0) {
ic = 0.3 + 0.7*(w – 350.0) / (420.0 – 350.0);
} else if(w >= 420.0 && w <= 700.0) {
ic = 1.0;
} else if(w > 700.0 && w <= 780.0) {
ic = 0.3 + 0.7*(780.0 – w) / (780.0 – 700.0);
} else {
#ifndef INVISIBLE_TRANSPARENT
ic = 0.5;
alpha = 0.3;
#else
alpha = 0.0;
#endif
}
ic *= 255.0;

return vec4(ic * red / 255.0, ic * green / 255.0, ic * blue / 255.0, alpha);
}

void main(){
vec4 color = vec4(1.0);
float max = max(m_MaxWavelength, 0);
if(max == 0.0) {
max = 2000.0;
}
float min = max(m_MinWavelength, 0);

float texCoord;

#ifdef HORIZONTAL
texCoord = texCoord1.x;
#else
texCoord = texCoord1.y;
#endif

#ifdef REVERSE
texCoord = 1.0 – texCoord;
#endif

gl_FragColor = getColourFromWavelength((texCoord + min / max) * max);
}