#ifdef COLOR 
    uniform vec4 m_Color;
#endif

#ifdef COLORMAP
    uniform sampler2D m_ColorMap;
#endif

#ifdef BURNMAP
    uniform sampler2D m_BurnMap;

    #ifdef BURN_PARAMS
    uniform float m_Param;
    #endif
#endif

varying vec4 vertexPosition;
varying vec4 texCoord;


void main(){

    vec4 color = vec4(1.0);n
    
    #ifdef COLOR
        color = m_Color;
    #endif

    #ifdef COLORMAP
        color *= texture2D(m_ColorMap, texCoord);
    #endif

    #if defined(BURNMAP) && defined(PARAM)

    if(textureTD(m_BurnMap, texCoord).r < m_Param){
    discard;
    }

    #endif

gl_FragColor=color;
}