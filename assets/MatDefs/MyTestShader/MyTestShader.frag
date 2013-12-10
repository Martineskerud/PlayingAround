#ifdef COLOR
    uniform vec4 m_Color;
#endif
#ifdef TIME
    uniform int m_Time;
#endif

varying vec4 vertexPosition;
uniform float g_Time;

/*
* fragment shader template
*/
void main() {
    
    
    float centerDistance = sqrt(pow(vertexPosition.x, 2) + pow(vertexPosition.y, 2));
    float o = sin(g_Time);

    float r = 0.1;
    float g = tan((1.0 - o) * centerDistance); //  * sin(o * vertexPosition.x) * cos(o * vertexPosition.y)
    float b = 1.0;

    vec4 color = vec4(r, g, b, 1.0);   
   
    gl_FragColor = color; 
}

