package com.precisionhawk.mavlinkpoc;

import com.precisionhawk.mavlinkpoc.mavink.MAVLink;
import com.precisionhawk.mavlinkpoc.mavink.messages.common.*;
import jssc.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GcsBirdCommunication {
    final static Logger LOG = LoggerFactory.getLogger(GcsBirdCommunication.class);
    static SerialPort serialPort;
    static int count = 0;

    public static void main(String[] args) {
        serialPort = new SerialPort("COM1");// manually have to change OS specific ports. Here COM3 is on which RC box is connected.
        try {
            String[] portNames = SerialPortList.getPortNames();
            for(int i = 0; i < portNames.length; i++){
                LOG.info(portNames[i]);//gives you list of ports available on your system
            }
            serialPort.openPort();//Open port
            serialPort.setParams(115200, 0, 0, 0);//Set params
            int mask = SerialPort.MASK_RXCHAR + SerialPort.MASK_CTS + SerialPort.MASK_DSR;//Prepare mask
            serialPort.setEventsMask(mask);//Set mask
            serialPort.addEventListener(new SerialPortReader());//Add SerialPortEventListener
        } catch (SerialPortException ex) {
            LOG.error("Error Occured {}",ex);
        }
    }

    /*
     * In this class must implement the method serialEvent, through it we learn about 
     * events that happened to our port. But we will not report on all events but only 
     * those that we put in the mask. In this case the arrival of the data and change the 
     * status lines CTS and DSR
     */
    static class SerialPortReader implements SerialPortEventListener {
        int checksumIndex1, checksumIndex2;

        public void serialEvent(SerialPortEvent event) {

            if (event.isRXCHAR()) {//If data is available
                if (event.getEventValue() > 8) { //It returns number of byte it has received.
                    //LOG.info(String.valueOf(event.getEventValue()));

                    try {
                        byte receivedBuffer[] = serialPort.readBytes(event.getEventValue());
                        //LOG.info("length {}", receivedBuffer.length);
                        LOG.info("length {}", receivedBuffer[1]);
                        if ((receivedBuffer[1] == 9 || receivedBuffer[1] == 25) && receivedBuffer.length > 0) {

                            MAVLinkPayload mavLinkPayload = new MAVLinkPayload();
                            mavLinkPayload.getData().put(receivedBuffer, 6, receivedBuffer[1]);//*We are deserialising buffer from 6th byte because initial 6(0-5)bytes contain header info and last 2 bytes is of checksum*/
                            MAVLinkPacket mavLinkPacket = new MAVLinkPacket();
                            mavLinkPacket.payload = mavLinkPayload;
                            mavLinkPacket.len = receivedBuffer[1];
                            mavLinkPacket.seq = receivedBuffer[2];
                            mavLinkPacket.sysid = receivedBuffer[3];
                            mavLinkPacket.compid = receivedBuffer[4];
                            mavLinkPacket.msgid = receivedBuffer[5];
                            byte[] calculatedBuffer = mavLinkPacket.encodePacket();
                            checksumIndex1 = calculatedBuffer.length - 2; // last second byte of packet contains LSB(Least Significant Bit) of CRC
                            checksumIndex2 = calculatedBuffer.length - 1; // last byte of packet contains MSB(Most Significant Bit) of CRC
                            if (calculatedBuffer[checksumIndex1] == receivedBuffer[checksumIndex1] && calculatedBuffer[checksumIndex2] == receivedBuffer[checksumIndex2]) { // Checking CRC

                                if (calculatedBuffer[5] == msg_heartbeat.MAVLINK_MSG_ID_HEARTBEAT) {
                                    msg_heartbeat msg_heartbeat = new msg_heartbeat();
                                    msg_heartbeat.unpack(mavLinkPayload);
                                    LOG.info("Heart_Beat message received " + "\n Custom mode:" + msg_heartbeat.custom_mode + "\n" +
                                            " Autopilot mode: " + (int) msg_heartbeat.autopilot + "\n Type:" + (int) msg_heartbeat.type + "\n" +
                                            " BaseMode:" + (int) msg_heartbeat.base_mode + "\n System Status:" + (int) msg_heartbeat.system_status + "\n" +
                                            " MavlinkVersion:" + (int) msg_heartbeat.mavlink_version + "\n" + " Length " + calculatedBuffer[1] + "\n" + " sysid " + calculatedBuffer[3] + "\n" + " compid " + calculatedBuffer[4]);


                                    msg_heartbeat heartbeat = new msg_heartbeat(); /*Creating  heart_beat message */
                                    heartbeat.autopilot = MAVLink.MAV_AUTOPILOT_TYPE.MAV_AUTOPILOT_ARDUPILOTMEGA;
                                    heartbeat.system_status = MAVLink.MAV_STATE.MAV_STATE_ACTIVE;
                                    heartbeat.type = MAVLink.MAV_TYPE.MAV_GROUND;

                                    byte[] heartbeat_msg = heartbeat.pack().encodePacket(); /*Sending heart_beat message to bird*/
                                    serialPort.writeBytes(heartbeat_msg);
                                    count++;
                                    if (count == 3) {

                                        msg_param_request_list msg_param_request_list = new msg_param_request_list();/*To establish a connection GCS sends param request message to bird after it bird sends various onboard parameters*/
                                        msg_param_request_list.target_system = 1;
                                        msg_param_request_list.target_component = 1;
                                        serialPort.writeBytes(msg_param_request_list.pack().encodePacket());
                                    }
                                }


                                if (calculatedBuffer[5] == msg_param_value.MAVLINK_MSG_ID_PARAM_VALUE) { /*To establish a connection between two entities Bird sends various parameters*/
                                    msg_param_value msg_param_value = new msg_param_value();
                                    msg_param_value.unpack(mavLinkPayload);
                                    LOG.info("Param message received " + "\n Param index:" + msg_param_value.param_index + "\n Param Id:" + msg_param_value.param_id + "\n Param Id:" + msg_param_value.toString());
                                }
                            }

                        }
                    } catch (SerialPortException ex) {
                        LOG.error("Error Occured {}",ex);
                    }
                }
            }
        }
    }

}